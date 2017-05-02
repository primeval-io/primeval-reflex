package io.primeval.reflect.proxy.bytecode;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class InterceptionHandlerGenerator implements Opcodes {

    public static final String SUFFIX_START = "$handlerFor$";

    public static String getName(Class<?> classToProxy, Method method, int methodId) {
        String suffix = SUFFIX_START + method.getName() + methodId;
        return classToProxy.getName() + suffix;
    }

    public static byte[] generateMethodInterceptionHandler(Class<?> classToProxy, Method method,
            int methodId) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        FieldVisitor fv;
        MethodVisitor mv;

        String classToProxyDescriptor = Type.getDescriptor(classToProxy);
        String classToProxyInternalName = Type.getInternalName(classToProxy);

        String suffix = SUFFIX_START + method.getName() + methodId;
        String selfClassInternalName = classToProxyInternalName + suffix;
        String selfClassDescriptor = ReflectUtils.makeSuffixClassDescriptor(classToProxyDescriptor, suffix);

        Class<?> returnType = method.getReturnType();

        Class<?> interceptionHandlerClass = ReflectUtils.getInterceptionHandlerClass(returnType);
        String interceptionHandlerClassInternalName = Type.getInternalName(interceptionHandlerClass);

        String argsSuffix = "$argsFor$" + method.getName() + methodId;
        String argsClassInternalName = classToProxyInternalName + argsSuffix;
        String argsClassDescriptor = ReflectUtils.makeSuffixClassDescriptor(classToProxyDescriptor, argsSuffix);

        Parameter[] parameters = method.getParameters();
        cw.visit(52, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, selfClassInternalName, null, "java/lang/Object",
                new String[] { interceptionHandlerClassInternalName });

        cw.visitSource("@proxyhandler@primeval", null);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "delegate", classToProxyDescriptor, null, null);
            fv.visitEnd();
        }
        if (parameters.length > 0) {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "arguments", argsClassDescriptor,
                    null, null);
            fv.visitEnd();
        }

        {
            if (parameters.length == 0) {
                mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + classToProxyDescriptor + ")V", null,
                        null);
            } else {
                mv = cw.visitMethod(ACC_PUBLIC, "<init>",
                        "(" + classToProxyDescriptor + argsClassDescriptor + ")V", null,
                        null);
            }
            mv.visitParameter("delegate", 0);
            if (parameters.length > 0) {
                mv.visitParameter("arguments", 0);
            }
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(10, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, selfClassInternalName, "delegate",
                    classToProxyDescriptor);
            if (parameters.length > 0) {
                Label l1b = new Label();
                mv.visitLabel(l1b);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitFieldInsn(PUTFIELD, selfClassInternalName, "arguments", argsClassDescriptor);
            }
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(12, l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", selfClassDescriptor, null, l0,
                    l3, 0);
            mv.visitLocalVariable("delegate", classToProxyDescriptor, null, l0, l3,
                    1);
            if (parameters.length > 0) {
                mv.visitLocalVariable("arguments", argsClassDescriptor, null, l0, l3, 2);
            }
            mv.visitMaxs(-1, -1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "getArguments", "()Lio/primeval/reflect/proxy/arguments/Arguments;", null,
                    null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            if (parameters.length == 0) {
                mv.visitFieldInsn(GETSTATIC, "io/primeval/reflect/proxy/arguments/Arguments", "EMPTY_ARGUMENTS",
                        "Lio/primeval/reflect/proxy/arguments/Arguments;");
            } else {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, selfClassInternalName, "arguments", argsClassDescriptor);
            }
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", selfClassDescriptor, null, l0,
                    l1, 0);
            mv.visitMaxs(-1, -1);
            mv.visitEnd();
        }
        String methodDescriptor = Type.getMethodDescriptor(method);

        Class<?> invokeReturnType = returnType.isPrimitive() ? returnType : Object.class;
        String invokeReturnTypeDescriptor = Type.getDescriptor(invokeReturnType);

        {

            mv = cw.visitMethod(ACC_PUBLIC, "invoke",
                    "(Lio/primeval/reflect/proxy/arguments/Arguments;)" + invokeReturnTypeDescriptor, null,
                    new String[] { "java/lang/Exception" });
            mv.visitParameter("arguments", 0);
            mv.visitCode();

            if (parameters.length == 0) {
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitLineNumber(21, l0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate",
                        classToProxyDescriptor);
                mv.visitMethodInsn(INVOKEVIRTUAL, classToProxyInternalName, method.getName(),
                        "()" + Type.getDescriptor(returnType), false);
                mv.visitInsn(TypeUtils.getReturnCode(returnType));
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", selfClassDescriptor, null, l0,
                        l1, 0);
                mv.visitLocalVariable("arguments", "Lio/primeval/reflect/proxy/arguments/Arguments;", null, l0, l1, 1);
            } else {

                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(INSTANCEOF, argsClassInternalName);
                Label l1 = new Label();
                mv.visitJumpInsn(IFEQ, l1);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, argsClassInternalName);
                mv.visitVarInsn(ASTORE, 2);
                Label l3 = new Label();
                mv.visitLabel(l3);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate",
                        classToProxyDescriptor);

                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Class<?> paramType = parameter.getType();
                    mv.visitVarInsn(ALOAD, 2);
                    String paramTypeDesc = Type.getDescriptor(paramType);
                    mv.visitFieldInsn(GETFIELD, argsClassInternalName, parameter.getName(),
                            paramTypeDesc);
                }
                mv.visitMethodInsn(INVOKEVIRTUAL, classToProxyInternalName, method.getName(), methodDescriptor,
                        false);
                mv.visitInsn(TypeUtils.getReturnCode(returnType));
                mv.visitLabel(l1);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate", classToProxyDescriptor);
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Class<?> paramType = parameter.getType();
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(parameter.getName());
                    Class<?> methodTypeDesc = paramType.isPrimitive() ? paramType : Object.class;
                    mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/reflect/proxy/arguments/Arguments",
                            getArgumentGetter(paramType), "(Ljava/lang/String;)" + Type.getDescriptor(methodTypeDesc),
                            true);
                    if (!paramType.isPrimitive()) {
                        mv.visitTypeInsn(CHECKCAST, Type.getInternalName(paramType));
                    }
                }

                mv.visitMethodInsn(INVOKEVIRTUAL, classToProxyInternalName, method.getName(), methodDescriptor,
                        false);
                mv.visitInsn(TypeUtils.getReturnCode(returnType));
                Label l4 = new Label();
                mv.visitLabel(l4);
                mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l4,
                        0);
                mv.visitLocalVariable("arguments", "Lio/primeval/reflect/proxy/arguments/Arguments;", null, l0, l4, 1);
                mv.visitLocalVariable("customArgs", argsClassDescriptor, null, l3, l1, 2);

            }

            mv.visitMaxs(-1, -1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "invoke", "()" + invokeReturnTypeDescriptor, null,
                    new String[] { "java/lang/Throwable" });
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            Label l1 = new Label();
            if (parameters.length > 0) {
                mv.visitVarInsn(ALOAD, 0);

                mv.visitFieldInsn(GETFIELD, selfClassInternalName, "arguments", argsClassDescriptor);
                mv.visitVarInsn(ASTORE, 1);
                mv.visitLabel(l1);
            }
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate", classToProxyDescriptor);
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class<?> paramType = parameter.getType();
                mv.visitVarInsn(ALOAD, 1);
                mv.visitFieldInsn(GETFIELD, argsClassInternalName, parameter.getName(),
                        Type.getDescriptor(paramType));
            }
            mv.visitMethodInsn(INVOKEVIRTUAL, classToProxyInternalName, method.getName(), methodDescriptor,
                    false);
            mv.visitInsn(TypeUtils.getReturnCode(returnType));
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l2, 0);
            mv.visitLocalVariable("customArgs", argsClassDescriptor, null, l1, l2, 1);
            mv.visitMaxs(-1, -1);
            mv.visitEnd();
        }

        cw.visitEnd();

        return cw.toByteArray();
    }

    private static String getArgumentGetter(Class<?> type) {
        if (type == int.class) {
            return "intArg";
        } else if (type == short.class) {
            return "shortArg";
        } else if (type == boolean.class) {
            return "booleanArg";
        } else if (type == char.class) {
            return "characterArg";
        } else if (type == byte.class) {
            return "byteArg";
        } else if (type == long.class) {
            return "longArg";
        } else if (type == double.class) {
            return "doubleArg";
        } else if (type == float.class) {
            return "floatArg";
        } else if (type == void.class) {
            throw new AssertionError("can't get a void!");
        } else {
            return "objectArg";
        }
    }

}
