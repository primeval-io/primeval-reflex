package io.primeval.reflect.proxy.bytecode;

import static io.primeval.reflect.proxy.bytecode.TypeUtils.IRETURN_TYPES;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getLoadCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getReturnCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getTypeSize;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class ProxyClassMethodInterceptionHandlerGenerator implements Opcodes {

    public static final String SUFFIX_START = "$handlerFor$";

    public static String getName(Class<?> targetParentClass, Method method, int methodId) {
        String suffix = SUFFIX_START + method.getName() + methodId;
        return targetParentClass.getName() + suffix;
    }

    public static byte[] generateMethodInterceptionHandler(Class<?> targetParentClass, Method method, int methodId)
            throws Exception {
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class) {
            return generateVoidMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == int.class) {
            return generateIntMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == short.class) {
            return generateShortMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == double.class) {
            return generateDoubleMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == float.class) {
            return generateFloatMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == char.class) {
            return generateCharMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == long.class) {
            return generateLongMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == byte.class) {
            return generateByteMethodInterceptionHandler(targetParentClass, method, methodId);
        } else if (returnType == boolean.class) {
            return generateBooleanMethodInterceptionHandler(targetParentClass, method, methodId);
        } else {
            return generateObjectMethodInterceptionHandler(targetParentClass, method, methodId);
        }
    }

    private static byte[] generateObjectMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, "io/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler",
                "Ljava/lang/Object;Lio/primeval/reflect/proxy/handler/ObjectInterceptionHandler<Ljava/lang/String;>;",
                "java/lang/Object", new String[] { "io/primeval/reflect/proxy/handler/ObjectInterceptionHandler" });

        cw.visitSource("M0ObjectInterceptionHandler.java", null);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "delegate",
                    "Lio/primeval/reflect/proxy/theory/TheoreticalDelegate;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lio/primeval/reflect/proxy/theory/TheoreticalDelegate;)V", null,
                    null);
            mv.visitParameter("delegate", 0);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(10, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(11, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, "io/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler", "delegate",
                    "Lio/primeval/reflect/proxy/theory/TheoreticalDelegate;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(12, l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "Lio/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler;", null, l0,
                    l3, 0);
            mv.visitLocalVariable("delegate", "Lio/primeval/reflect/proxy/theory/TheoreticalDelegate;", null, l0, l3,
                    1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getArguments", "()Lio/primeval/reflect/proxy/arguments/Arguments;", null,
                    null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(16, l0);
            mv.visitFieldInsn(GETSTATIC, "io/primeval/reflect/proxy/arguments/Arguments", "EMPTY_ARGUMENTS",
                    "Lio/primeval/reflect/proxy/arguments/Arguments;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lio/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler;", null, l0,
                    l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "invoke",
                    "(Lio/primeval/reflect/proxy/arguments/Arguments;)Ljava/lang/String;", null,
                    new String[] { "java/lang/Exception" });
            mv.visitParameter("arguments", 0);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(21, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, "io/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler", "delegate",
                    "Lio/primeval/reflect/proxy/theory/TheoreticalDelegate;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "io/primeval/reflect/proxy/theory/TheoreticalDelegate", "hello",
                    "()Ljava/lang/String;", false);
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lio/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler;", null, l0,
                    l1, 0);
            mv.visitLocalVariable("arguments", "Lio/primeval/reflect/proxy/arguments/Arguments;", null, l0, l1, 1);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "invoke",
                    "(Lio/primeval/reflect/proxy/arguments/Arguments;)Ljava/lang/Object;", null,
                    new String[] { "java/lang/Throwable" });
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(1, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "io/primeval/reflect/proxy/theory/M0ObjectInterceptionHandler", "invoke",
                    "(Lio/primeval/reflect/proxy/arguments/Arguments;)Ljava/lang/String;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    private static byte[] generateBooleanMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateByteMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateLongMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateCharMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateFloatMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateDoubleMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateShortMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateIntMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    private static byte[] generateVoidMethodInterceptionHandler(Class<?> targetParentClass, Method method,
            int methodId) {
        // TODO Auto-generated method stub
        return null;
    }

    public static byte[] ee(Class<?> targetParentClass, Method method, int methodId) {
        ClassWriter cw = new ClassWriter(0);

        String wovenClassDescriptor = Type.getDescriptor(targetParentClass);
        String wovenClassInternalName = Type.getInternalName(targetParentClass);

        String suffix = SUFFIX_START + method.getName() + methodId;
        String selfClassInternalName = wovenClassInternalName + suffix;
        String selfClassDescriptor = makeSelfClassDescriptor(wovenClassDescriptor, suffix);

        String updaterClassInternalName = wovenClassInternalName + ProxyClassArgsUpdaterGenerator.SUFFIX_START
                + method.getName() + methodId;

        String constDesc = Type.getMethodDescriptor(Type.VOID_TYPE,
                Stream.concat(Stream.of(List.class), Stream.of(method.getParameterTypes())).map(Type::getType)
                        .toArray(Type[]::new));

        cw.visit(52, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, selfClassInternalName, null, "java/lang/Object",
                new String[] { "io/primeval/aspecio/aspect/interceptor/arguments/Arguments" });
        Parameter[] parameters = method.getParameters();

        generateFields(method, cw, parameters);
        generateConstructor(method, cw, selfClassInternalName, selfClassDescriptor, constDesc, parameters);
        generateHashCodeMethod(cw, selfClassInternalName, selfClassDescriptor, parameters);
        generateEqualsMethod(cw, selfClassInternalName, selfClassDescriptor, parameters);
        generateToStringMethod(cw, selfClassInternalName, selfClassDescriptor, parameters);

        generateUpdaterMethod(cw, selfClassInternalName, selfClassDescriptor, updaterClassInternalName, constDesc,
                parameters);

        generateParametersGetter(cw, selfClassInternalName, selfClassDescriptor);
        generateArgumentGetters(cw, selfClassInternalName, selfClassDescriptor, parameters);
        cw.visitEnd();

        return cw.toByteArray();
    }

    private static void generateUpdaterMethod(ClassWriter cw, String selfClassInternalName, String selfClassDescriptor,
            String updaterClassInternalName,
            String constDesc, Parameter[] parameters) {
        MethodVisitor mv;

        mv = cw.visitMethod(ACC_PUBLIC, "updater",
                "()Lio/primeval/aspecio/aspect/interceptor/arguments/ArgumentsUpdater;", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitTypeInsn(NEW, updaterClassInternalName);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "parameters", "Ljava/util/List;");
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, parameter.getName(),
                    Type.getDescriptor(parameter.getType()));
        }
        mv.visitMethodInsn(INVOKESPECIAL, updaterClassInternalName, "<init>", constDesc, false);
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l1, 0);
        mv.visitMaxs(parameters.length + 3, 1);
        mv.visitEnd();
    }

    private static void generateFields(Method method, ClassWriter cw, Parameter[] parameters) {
        FieldVisitor fv;
        cw.visitSource(method.getName() + "Args@@aspecio", null);

        fv = cw.visitField(ACC_PUBLIC + ACC_FINAL, "parameters", "Ljava/util/List;",
                "Ljava/util/List<Ljava/lang/reflect/Parameter;>;",
                null);
        fv.visitEnd();

        for (Parameter p : parameters) {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL, p.getName(), Type.getDescriptor(p.getType()), null, null);
            fv.visitEnd();
        }
    }

    private static void generateConstructor(Method method, ClassWriter cw, String selfClassInternalName,
            String selfClassDescriptor,
            String constDesc, Parameter[] parameters) {
        MethodVisitor mv;

        mv = cw.visitMethod(ACC_PUBLIC, "<init>", constDesc, null, null);
        mv.visitParameter("parameters", 0);
        int paramCount = parameters.length;
        int[] paramIndices = new int[paramCount];
        int nextVarIndex = 2; // 0 = "this", 1 = "parameters"
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            paramIndices[i] = nextVarIndex;
            mv.visitParameter(param.getName(), 0);
            nextVarIndex += getTypeSize(param.getType());
        }
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, selfClassInternalName, "parameters", "Ljava/util/List;");

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            mv.visitVarInsn(ALOAD, 0);
            Class<?> paramType = parameter.getType();
            mv.visitVarInsn(getLoadCode(paramType), paramIndices[i]);
            mv.visitFieldInsn(PUTFIELD, selfClassInternalName, parameter.getName(), Type.getDescriptor(paramType));
        }

        mv.visitInsn(RETURN);
        Label l7 = new Label();
        mv.visitLabel(l7);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l7, 0);
        mv.visitLocalVariable("parameters", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/reflect/Parameter;>;", l0,
                l7, 1);

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            mv.visitLocalVariable(parameter.getName(), Type.getDescriptor(parameter.getType()), null, l0, l7,
                    paramIndices[i]);
        }
        mv.visitMaxs(2, nextVarIndex);
        mv.visitEnd();
    }

    private static void generateHashCodeMethod(ClassWriter cw, String selfClassInternalName, String selfClassDescriptor,
            Parameter[] parameters) {
        MethodVisitor mv;
        mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
        mv.visitCode();

        // int result = 1;

        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitInsn(ICONST_1);
        mv.visitVarInsn(ISTORE, 1);

        for (Parameter param : parameters) {
            Class<?> type = param.getType();
            mv.visitIntInsn(BIPUSH, 31);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(IMUL);
            mv.visitVarInsn(ALOAD, 0);
            Type typeType = Type.getType(type);
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, param.getName(), typeType.getDescriptor());
            if (type.isPrimitive()) {
                Class<?> boxed = TypeUtils.getBoxed(type);
                mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(boxed), "hashCode",
                        Type.getMethodDescriptor(Type.INT_TYPE, typeType), false);

            } else {
                mv.visitMethodInsn(INVOKESTATIC, "java/util/Objects", "hashCode", "(Ljava/lang/Object;)I", false);
            }
            mv.visitInsn(IADD);
            mv.visitVarInsn(ISTORE, 1);
        }

        mv.visitVarInsn(ILOAD, 1);
        mv.visitInsn(IRETURN);
        Label l7 = new Label();
        mv.visitLabel(l7);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l7, 0);
        mv.visitLocalVariable("result", "I", null, l0, l7, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    private static void generateEqualsMethod(ClassWriter cw, String selfClassInternalName, String selfClassDescriptor,
            Parameter[] parameters) {
        MethodVisitor mv;
        mv = cw.visitMethod(ACC_PUBLIC, "equals", "(Ljava/lang/Object;)Z", null, null);
        mv.visitParameter("obj", 0);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        Label l1 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, l1);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitLabel(l1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 1);
        Label l3 = new Label();
        mv.visitJumpInsn(IFNONNULL, l3);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitInsn(ICONST_0);
        mv.visitInsn(IRETURN);
        mv.visitLabel(l3);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        Label l5 = new Label();
        mv.visitJumpInsn(IF_ACMPEQ, l5);
        Label l6 = new Label();
        mv.visitLabel(l6);
        mv.visitInsn(ICONST_0);
        mv.visitInsn(IRETURN);
        mv.visitLabel(l5);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, selfClassInternalName);
        mv.visitVarInsn(ASTORE, 2);
        Label l7 = new Label();
        mv.visitLabel(l7);
        Label l8 = new Label();

        for (Parameter param : parameters) {
            mv.visitVarInsn(ALOAD, 0);
            Class<?> type = param.getType();
            String paramName = param.getName();
            String typeDesc = Type.getDescriptor(type);
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, paramName, typeDesc);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, paramName, Type.getDescriptor(type));
            if (type.isPrimitive()) {
                if (IRETURN_TYPES.contains(type)) {
                    mv.visitJumpInsn(IF_ICMPNE, l8);
                } else if (type == float.class) {
                    mv.visitInsn(FCMPL);
                    mv.visitJumpInsn(IFNE, l8);
                } else if (type == double.class) {
                    mv.visitInsn(DCMPL);
                    mv.visitJumpInsn(IFNE, l8);
                } else if (type == long.class) {
                    mv.visitInsn(LCMP);
                    mv.visitJumpInsn(IFNE, l8);
                }
            } else {
                mv.visitMethodInsn(INVOKESTATIC, "java/util/Objects", "equals",
                        "(Ljava/lang/Object;Ljava/lang/Object;)Z", false);
                mv.visitJumpInsn(IFEQ, l8);
            }
        }
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitLabel(l8);
        mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { selfClassInternalName }, 0, null);
        mv.visitInsn(ICONST_0);
        mv.visitInsn(IRETURN);
        Label l9 = new Label();
        mv.visitLabel(l9);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l9, 0);
        mv.visitLocalVariable("obj", "Ljava/lang/Object;", null, l0, l9, 1);
        mv.visitLocalVariable("other", selfClassDescriptor, null, l7, l9, 2);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    private static void generateToStringMethod(ClassWriter cw, String selfClassInternalName, String selfClassDescriptor,
            Parameter[] parameters) {
        MethodVisitor mv;
        mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(Type.getType(selfClassDescriptor));
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getSimpleName", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;",
                false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitLdcInsn(" [");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            mv.visitLdcInsn(param.getName() + "=");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                    false);
            mv.visitVarInsn(ALOAD, 0); // this
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, param.getName(), Type.getDescriptor(param.getType()));
            Class<?> paramType = param.getType();
            if (paramType.isPrimitive()) {
                // special case with StringBuilder, no specific method we default to append(int)
                if (paramType == short.class || paramType == byte.class) {
                    paramType = int.class;
                }
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        Type.getMethodDescriptor(Type.getType(StringBuilder.class), Type.getType(paramType)), false);
            } else {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
            }
            if (i + 1 < parameters.length) {
                mv.visitLdcInsn(", ");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false);
            }
        }
        mv.visitLdcInsn("]");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l1, 0);
        mv.visitMaxs(3, 1);
        mv.visitEnd();
    }

    private static void generateParametersGetter(ClassWriter cw, String selfClassInternalName,
            String selfClassDescriptor) {
        MethodVisitor mv;
        mv = cw.visitMethod(ACC_PUBLIC, "parameters", "()Ljava/util/List;",
                "()Ljava/util/List<Ljava/lang/reflect/Parameter;>;", null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "parameters", "Ljava/util/List;");
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private static void generateArgumentGetters(ClassWriter cw, String selfClassInternalName,
            String selfClassDescriptor,
            Parameter[] parameters) {
        MethodVisitor mv;
        {
            mv = cw.visitMethod(ACC_PUBLIC, "objectArg", "(Ljava/lang/String;)Ljava/lang/Object;",
                    "<T:Ljava/lang/Object;>(Ljava/lang/String;)TT;", null);
            generateArgGetterCode(Object.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "intArg", "(Ljava/lang/String;)I", null, null);
            generateArgGetterCode(int.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "shortArg", "(Ljava/lang/String;)S", null, null);
            generateArgGetterCode(short.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "longArg", "(Ljava/lang/String;)J", null, null);
            generateArgGetterCode(long.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "byteArg", "(Ljava/lang/String;)B", null, null);
            generateArgGetterCode(byte.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "booleanArg", "(Ljava/lang/String;)Z", null, null);
            generateArgGetterCode(boolean.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "floatArg", "(Ljava/lang/String;)F", null, null);
            generateArgGetterCode(float.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "doubleArg", "(Ljava/lang/String;)D", null, null);
            generateArgGetterCode(double.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "charArg", "(Ljava/lang/String;)C", null, null);
            generateArgGetterCode(char.class, mv, selfClassInternalName, selfClassDescriptor, parameters);
        }
    }

    private static void generateArgGetterCode(Class<?> argType, MethodVisitor mv, String selfClassInternalName,
            String selfClassDescriptor,
            Parameter[] parameters) {
        Parameter[] matchingParams = Stream.of(parameters).filter(c -> argType.isAssignableFrom(c.getType()))
                .toArray(Parameter[]::new);
        mv.visitParameter("argName", 0);
        mv.visitCode();
        Label first = new Label();
        Label nextLabel = first;
        for (int i = 0; i < matchingParams.length; i++) {
            Parameter parameter = matchingParams[i];
            mv.visitLabel(nextLabel);
            if (i > 0) {
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            }
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn(parameter.getName());
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            nextLabel = new Label();
            mv.visitJumpInsn(IFEQ, nextLabel);
            Label l2 = new Label();
            mv.visitLabel(l2);
            Class<?> paramType = parameter.getType();
            mv.visitVarInsn(ALOAD, 0); // this
            mv.visitFieldInsn(GETFIELD, selfClassInternalName, parameter.getName(), Type.getDescriptor(paramType));
            mv.visitInsn(getReturnCode(paramType));
        }
        // final else
        mv.visitLabel(nextLabel);
        if (matchingParams.length > 0) {
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        genExceptionThrowForUnknownParam(argType.getSimpleName(), mv);
        Label last = new Label();
        mv.visitLabel(last);
        mv.visitLocalVariable("this", selfClassDescriptor, null, first, last, 0);
        mv.visitLocalVariable("argName", "Ljava/lang/String;", null, first, last, 1);

        mv.visitMaxs(5, 2);
        mv.visitEnd();
    }

    private static void genExceptionThrowForUnknownParam(String paramType, MethodVisitor mv) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No " + paramType + " parameter named ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V",
                false);
        mv.visitInsn(ATHROW);
    }

    private static String makeSelfClassDescriptor(String wovenClassDescriptor, String suffix) {
        StringBuilder buf = new StringBuilder();
        buf.append(wovenClassDescriptor, 0, wovenClassDescriptor.length() - 1); // omit
                                                                                // ';'
        buf.append(suffix);
        buf.append(';');
        return buf.toString();
    }
}
