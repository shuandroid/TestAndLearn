package com.chendroid.learning

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.chendroid.learning.asm.AnnotationClassVisitor
import com.chendroid.learning.asm.PreInflaterClassVisitor
import com.google.common.collect.ImmutableSet
import com.google.common.io.ByteStreams
import com.google.common.io.Files
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.com.amazonaws.services.s3.transfer.TransferManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.nio.file.Path
import java.util.function.Predicate
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * @author : zhaochen
 * @date : 2021/7/12
 * @description : 插件
 */

class PreInflaterTransform extends Transform {

    private final Project project

    private static final Set<String> inflaters = new HashSet<>()

    PreInflaterTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "collectPreInflateTransform"
    }

    /**
     * 只处理 .class
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }


    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return ImmutableSet.of(QualifiedContent.Scope.PROVIDED_ONLY, QualifiedContent.Scope.TESTED_CODE)
    }

    def collectClassPatch = { path ->
        String classPath = path.substring(0, path.length() - '.class'.length())
        if (isPreInflater(classPath)) {
            inflaters.add(classPath)
        }
        classPath
    }

    def isPreInflater = { className ->
        className.endsWith('$R2InflaterMapper')
    }

    /**
     * todo 为了获取～
     */
    def getR2PreInflaterMapperFromFile = { Path dirPath, Path classPath ->
        collectClassPatch(dirPath.relativize(classPath).toString())
    }

    def getR2PreInflaterMapperFromJar = { File jarFile ->
        new ZipFile(jarFile).stream()
                .map { entry -> entry.name }
                .filter { p -> p.endsWith(".class") }
                .map { p -> collectClassPatch(p) }
                .collect()
    }

    /**
     * ASM  注入代码这里
     *
     */
    def injectMethod = { classBytes ->
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        final PreInflaterClassVisitor visitor = new PreInflaterClassVisitor(writer, inflaters)
        final ClassReader reader = new ClassReader(classBytes)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        // 输出
        final TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        if (!transformInvocation.isIncremental()) {
            // 如果不支持增量，则删除已有
            outputProvider.deleteAll()
        }

        File preInflaterJarFile = null

        boolean isPreInflaterJarFound = false
        // 想办法匹配， Pattern 正则匹配类名, 用来检测 是否有我们的 PreInflater 包
        final Pattern preInflaterPattern = Pattern.compile("com.chendroid.learning:preinflater:(.*)")
        // 这是方法，后续根据该条件来判断是否是我们的 jar 包
        final Predicate<String> isPreInflaterJar = {
            jarName -> jarName == ':preinflater' || preInflaterPattern.matcher(jarName).matches()
        }

        // 输入的数据
        transformInvocation.referencedInputs.each { input ->
            input.directoryInputs.each { directoryInput ->
                final Path dirPath = directoryInput.file.toPath()
                FileUtils.getAllFiles(directoryInput.file).stream()
                        .map({ file -> getR2PreInflaterMapperFromFile(dirPath, file.toPath()) })
                        .collect()

            }
        }

        transformInvocation.inputs.each { input ->

            input.directoryInputs.each { directoryInput ->
                final Path dirPath = directoryInput.file.toPath()

                FileUtils.getAllFiles(directoryInput.file).stream()
                        .map({ file -> getR2PreInflaterMapperFromFile(dirPath, file.toPath()) })
                        .collect()

                injectAttachBaseContextClassFile(outputProvider, directoryInput)
            }

            input.jarInputs.each { jarInput ->
                FileUtils.getAllFiles(jarInput.file).stream().map { file ->
                    getR2PreInflaterMapperFromJar(file)
                }
                        .collect()

                def outJarFile = injectAttachBaseContextClassJar(outputProvider, jarInput)
                if (!isPreInflaterJarFound && isPreInflaterJar.test(jarInput.name)) {
                    preInflaterJarFile = outJarFile
                    isPreInflaterJarFound = true
                }

            }
        }

        if (isPreInflaterJarFound) {
            // 如果找到了 jar 包，则
            injectCollectMethod(preInflaterJarFile)
        }
    }


    def injectCollectMethod = { File preInflaterJarFile ->

        File tmpFile = new File(project.buildDir, String.join(File.separatorChar.toString(), 'tmp', 'inflater', 'inflater-tmp.jar'))
        Files.createParentDirs(tmpFile)

        new ZipInputStream(new FileInputStream(preInflaterJarFile)).withCloseable { zis ->
            new ZipOutputStream(new FileOutputStream(tmpFile)).withCloseable { zos ->
                ZipEntry entry
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.name.endsWith('PreInflaterManager.class')) {

                        zos.putNextEntry(new ZipEntry(entry.name))
                        zos.write(injectMethod(ByteStreams.toByteArray(zis)))
                    } else {
                        zos.putNextEntry(entry)
                        ByteStreams.copy(zis, zos)
                    }
                    zos.closeEntry()
                    zis.closeEntry()
                }
            }
        }
        FileUtils.copyFile(tmpFile, preInflaterJarFile)
        FileUtils.delete(tmpFile)
    }


    def injectAttachBaseContextClassJar = { TransformOutputProvider outputProvider, JarInput jarFile ->
        File tmpFile = new File(project.buildDir,
                String.join(File.separatorChar.toString(), 'tmp', 'inflater', jarFile.file.name + '-tmp.jar'))
        Files.createParentDirs(tmpFile)

        new ZipInputStream(new FileInputStream(jarFile.file)).withCloseable { zis ->
            new ZipOutputStream(new FileOutputStream(tmpFile)).withCloseable { zos ->
                ZipEntry entry
                while ((entry = zis.getNextEntry()) != null) {
                    byte[] modified = injectClass(ByteStreams.toByteArray(zis))
                    zos.putNextEntry(new ZipEntry(entry.name))
                    zos.write(modified)
                    zos.closeEntry()
                    zis.closeEntry()
                }
            }
        }

        def dest = outputProvider.getContentLocation(
                jarFile.name,
                jarFile.contentTypes,
                jarFile.scopes,
                Format.JAR
        )
        FileUtils.copyFile(tmpFile, dest)
        FileUtils.delete(tmpFile)

        return dest
    }


    /**
     * 向 Activity 的 attachBaseContext() 方法中写入代码 ;
     *
     */
    def injectAttachBaseContextClassFile = { TransformOutputProvider outputProvider, DirectoryInput directoryInput ->

        if (directoryInput.file.isDirectory) {
            directoryInput.file.eachFileRecurse { file ->
                if (file.isFile()) {
                    // todo 等待后续使用
                    // 修改后的文件
                    byte[] modified = injectClass(file.bytes)
                    // 这里应该是 file.parentFile.absolutePath
                    File destFile = new File(file.parentFile.absoluteFile, file.name)
                    FileOutputStream fileOutputStream = new FileOutputStream(destFile)
                    fileOutputStream.write(modified)
                    fileOutputStream.close()
                }
            }
        }

        def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)

    }

    // 注入的 class
    def injectClass = { byte[] contents ->

        try {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
            // todo
            ClassVisitor visitor = new AnnotationClassVisitor(Opcodes.ASM4, writer)
            ClassReader reader = new ClassReader(contents)
            reader.accept(visitor, ClassReader.EXPAND_FRAMES)
            writer.toByteArray()
        } catch (ignored) {
            contents
        }
    }

}