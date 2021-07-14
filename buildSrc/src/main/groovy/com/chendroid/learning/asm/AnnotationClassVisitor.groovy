package com.chendroid.learning.asm

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * 用于访问类，  class
 */
class AnnotationClassVisitor extends ClassVisitor {

    def isHasPreInflaterAnnotation = false

    def isHasAttachBaseContext = false

    def superName

    AnnotationClassVisitor(int api, ClassVisitor cv) {
        super(api, cv)
    }

    /**
     * 需要注意的方法名
     */
    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
        isHasAttachBaseContext = (name == 'attachBaseContext')

        if (isHasAttachBaseContext && isHasPreInflaterAnnotation) {
            return new AnnotationMethodVisitor(api, mv, access, name, desc, superName)
        }
        return mv
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        // todo 需要引入 PreInflater.class ,即 添加 PreInflater 的 annotation 的依赖
        isHasPreInflaterAnnotation = Type.getDescriptor(PreInflater.class).equals(desc)

        return super.visitAnnotation(desc, visible)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.superName = superName
    }

    @Override
    void visitEnd() {
        // todo 待补充
        super.visitEnd()
    }
}