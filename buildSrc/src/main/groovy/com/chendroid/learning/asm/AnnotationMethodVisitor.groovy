package com.chendroid.learning.asm

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 用于访问类方法
 */
class AnnotationMethodVisitor extends AdviceAdapter {

    def superName

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link org.objectweb.asm.Opcodes#ASM4} or {@link org.objectweb.asm.Opcodes#ASM5}.
     * @param mv
     *            the method visitor to which this adapter delegates calls.
     * @param access
     *            the method's access flags (see {@link org.objectweb.asm.Opcodes}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link org.objectweb.asm.Type Type}).
     */
    protected
    AnnotationMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, String superName) {
        super(api, mv, access, name, desc)
        this.superName = superName
    }

    @Override
    void visitCode() {
        // 使用 kotlin bytecode 查看，或者 ASM bytecode viewer 查看
        mv.visitVarInsn(ALOAD, 0)
        mv.visitVarInsn(ALOAD, 1)
        mv.visitMethodInsn(INVOKESTATIC, "com.chendroid.preinflater/AsyncWrapperLayoutInflater", "getInstance", "(Landroid/content/Context;)Lcom/chendroid/preinflater/AsyncWrapperLayoutInflater;", false)
        mv.visitVarInsn(ALOAD, 1)
        mv.visitMethodInsn(INVOKEVIRTUAL, "com/chendroid/preinflater/AsyncWrapperLayoutInflater", "wrapContext", "(Landroid/content/Context;)Landroid/content/Context;", false)
        mv.visitMethodInsn(INVOKESPECIAL, superName, "attachBaseContext", "(Landroid/content/Context;)V", false)
    }

}