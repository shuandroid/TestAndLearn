package com.chendroid.learning.asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class PreInflaterClassVisitor extends ClassVisitor {

    final Set<String> inflaters

    PreInflaterClassVisitor(ClassVisitor cv, Set<String> inflaters) {
        super(Opcodes.ASM4, cv)
        this.inflaters = inflaters
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
        return name != 'collectPreInflater' ? mv : new PreInflaterMethodVisitor(api, mv, access, name, desc, inflaters)
    }
}