package io.primeval.reflect.proxy.bytecode;

import static io.primeval.reflect.proxy.bytecode.TypeUtils.getBoxed;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getLoadCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getReturnCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getTypeSize;
import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import io.primeval.reflect.proxy.shared.Proxy;

public final class ProxyClassGenerator {

    public static final String PROXY_TARGET_CLASS_SUFFIX = "$Proxy$";

    public static String getName(Class<?> classToProxy) {
        return classToProxy.getName() + PROXY_TARGET_CLASS_SUFFIX;
    }

    public static byte[] create(Class<?> classToProxy, Class<?>[] interfaces, Method[] methods,
            Predicate<Method> shouldIntercept) throws Exception {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visitSource("@proxy@primeval", null);
        String classToProxyDescriptor = Type.getDescriptor(classToProxy);
        String classToProxyInternalName = Type.getInternalName(classToProxy);

        String selfClassInternalName = classToProxyInternalName + PROXY_TARGET_CLASS_SUFFIX;
        String selfClassDescriptor = makeTargetClassDescriptor(classToProxyDescriptor);

        Class<?> superclass = Proxy.class;
        // String superClassDescriptor = Type.getDescriptor(superclass);
        String superClassInternalName = Type.getInternalName(superclass);

        String typeSignature = TypeUtils.getTypeSignature(classToProxy);
        String[] itfs = Stream.of(interfaces).map(Type::getInternalName).toArray(String[]::new);
        cw.visit(52, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, selfClassInternalName, typeSignature,
                superClassInternalName, itfs);

        for (int i = 0; i < methods.length; i++) {
            if (!shouldIntercept.test(methods[i])) {
                continue;
            }
            {
                fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "meth" + i, "Ljava/lang/reflect/Method;", null,
                        null);
                fv.visitEnd();
            }
            {
                fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "cc" + i,
                        "Lio/primeval/reflect/proxy/CallContext;",
                        null, null);
                fv.visitEnd();
            }
        }
        // Delegate field
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "delegate", classToProxyDescriptor, null, null);
            fv.visitEnd();
        }

        // static init
        mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (!shouldIntercept.test(method)) {
                continue;
            }
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLdcInsn(Type.getType(classToProxyDescriptor));
            mv.visitLdcInsn(method.getName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            visitIntInsn(mv, parameterTypes.length);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
            for (int j = 0; j < parameterTypes.length; j++) {
                Class<?> type = parameterTypes[j];
                mv.visitInsn(DUP);
                visitIntInsn(mv, j);
                addTypeSpecial(mv, type);
                mv.visitInsn(AASTORE);
            }
            mv.visitMethodInsn(INVOKESTATIC, "io/primeval/reflect/proxy/shared/ProxyUtils",
                    "getMethodUnchecked",
                    "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
            mv.visitFieldInsn(PUTSTATIC, selfClassInternalName, "meth" + i, "Ljava/lang/reflect/Method;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitTypeInsn(NEW, "io/primeval/reflect/proxy/CallContext");
            mv.visitInsn(DUP);
            mv.visitLdcInsn(Type.getType(classToProxyDescriptor));
            mv.visitFieldInsn(GETSTATIC, selfClassInternalName, "meth" + i, "Ljava/lang/reflect/Method;");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "getParameters",
                    "()[Ljava/lang/reflect/Parameter;", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;",
                    false);
            mv.visitMethodInsn(INVOKESPECIAL, "io/primeval/reflect/proxy/CallContext", "<init>",
                    "(Ljava/lang/Class;Ljava/lang/reflect/Method;Ljava/util/List;)V", false);
            mv.visitFieldInsn(PUTSTATIC, selfClassInternalName, "cc" + i,
                    "Lio/primeval/reflect/proxy/CallContext;");
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();

        addTypeAnnotations(classToProxy, cw);

        // Constructor
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + classToProxyDescriptor + ")V", null, null);
            mv.visitParameter("delegate", 0);
            mv.visitCode();

            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superClassInternalName, "<init>", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, selfClassInternalName, "delegate", classToProxyDescriptor);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l3, 0);
            mv.visitLocalVariable("delegate", classToProxyDescriptor, null, l0, l3, 1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        // Methods
        for (int methId = 0; methId < methods.length; methId++) {
            Method method = methods[methId];
            if (shouldIntercept.test(method)) {
                writeInterceptedMethod(classToProxy, classToProxyInternalName, classToProxyDescriptor,
                        selfClassInternalName,
                        selfClassDescriptor,
                        cw, mv,
                        method, methId);
            } else {
                writeSimpleDelegationMethod(classToProxy, classToProxyInternalName, classToProxyDescriptor,
                        selfClassInternalName,
                        selfClassDescriptor,
                        cw, mv,
                        method, methId);
            }
        }

        cw.visitEnd();

        return cw.toByteArray();

    }

    private static void addTypeSpecial(MethodVisitor mv, Class<?> type) {
        if (type.isPrimitive()) {
            mv.visitFieldInsn(GETSTATIC, Type.getInternalName(getBoxed(type)), "TYPE", "Ljava/lang/Class;");

        } else {
            mv.visitLdcInsn(Type.getType(Type.getDescriptor(type)));
        }
    }

    private static String makeTargetClassDescriptor(String proxyClassDescriptor) {
        StringBuilder buf = new StringBuilder();
        buf.append(proxyClassDescriptor, 0, proxyClassDescriptor.length() - 1); // omit
                                                                                // ';'
        buf.append(PROXY_TARGET_CLASS_SUFFIX);
        buf.append(';');
        return buf.toString();
    }

    private static void addTypeAnnotations(Class<?> clazzToProxy, ClassWriter cw)
            throws IllegalAccessException, InvocationTargetException {
        for (Annotation ann : clazzToProxy.getDeclaredAnnotations()) {
            Class<? extends Annotation> annotationType = ann.annotationType();

            AnnotationVisitor av0 = cw.visitAnnotation(Type.getDescriptor(annotationType), true);

            addAnnotationTree(av0, ann, annotationType);
            av0.visitEnd();
        }
    }

    private static void addMethodAnnotations(Method method, MethodVisitor mv)
            throws IllegalAccessException, InvocationTargetException {
        for (Annotation ann : method.getAnnotations()) {
            Class<? extends Annotation> annotationType = ann.annotationType();

            AnnotationVisitor av0 = mv.visitAnnotation(Type.getDescriptor(annotationType), true);

            addAnnotationTree(av0, ann, annotationType);
            av0.visitEnd();
        }
    }

    private static void addMethodParameterAnnotations(Method method, MethodVisitor mv)
            throws IllegalAccessException, InvocationTargetException {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation ann : annotations) {
                Class<? extends Annotation> annotationType = ann.annotationType();

                AnnotationVisitor av0 = mv.visitParameterAnnotation(i, Type.getDescriptor(annotationType), true);
                addAnnotationTree(av0, ann, annotationType);
                av0.visitEnd();
            }
        }
    }

    private static void addAnnotationTree(AnnotationVisitor av0, Annotation ann,
            Class<? extends Annotation> annotationType)
            throws IllegalAccessException, InvocationTargetException {
        Method[] annMethods = annotationType.getDeclaredMethods();
        for (Method m : annMethods) {
            Class<?> returnType = m.getReturnType();

            if (returnType.isArray()) {
                Class<?> compType = returnType.getComponentType();
                String compDesc = Type.getDescriptor(compType);
                AnnotationVisitor avArray = av0.visitArray(m.getName());
                Object[] arr = (Object[]) m.invoke(ann);
                for (Object comp : arr) {
                    addAnnotation(null, compType, compDesc, avArray, comp);
                }
                avArray.visitEnd();
            } else {
                addAnnotation(m.getName(), returnType, Type.getDescriptor(returnType), av0, m.invoke(ann));
            }
        }
    }

    private static void addAnnotation(String name, Class<?> annotationMethodType, String typeDesc, AnnotationVisitor av,
            Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (Enum.class.isAssignableFrom(annotationMethodType)) {
            av.visitEnum(name, typeDesc, value.toString());
        } else if (annotationMethodType.isAnnotation()) {
            @SuppressWarnings("unchecked")
            Class<? extends Annotation> annType = (Class<? extends Annotation>) annotationMethodType;
            AnnotationVisitor annotationVisitor = av.visitAnnotation(name, typeDesc);
            addAnnotationTree(annotationVisitor, (Annotation) value, annType);
            annotationVisitor.visitEnd();
        } else {
            av.visit(name, value);
        }
    }

    private static void writeSimpleDelegationMethod(Class<?> clazzToProxy, String proxyClassInternalName,
            String proxyClassDescriptor,
            String selfClassInternalName, String selfClassDescriptor, ClassWriter cw,
            MethodVisitor mv, Method method, int methId) throws IllegalAccessException, InvocationTargetException {
        String methodDescriptor = Type.getMethodDescriptor(method);
        String methodName = method.getName();
        String methodSignature = TypeUtils.getMethodSignature(method);

        String[] exceptionTypes = null;
        Class<?>[] exceptionsClasses = method.getExceptionTypes();
        if (exceptionsClasses.length != 0) {
            exceptionTypes = Stream.of(exceptionsClasses).map(Type::getInternalName).toArray(String[]::new);
        }

        Class<?> returnType = method.getReturnType();

        mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL, methodName, methodDescriptor, methodSignature, exceptionTypes);
        Parameter[] parameters = method.getParameters();
        int paramCount = parameters.length;
        int[] paramIndices = new int[paramCount];
        int nextVarIndex = 1; // 0 = "this"
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            paramIndices[i] = nextVarIndex;
            mv.visitParameter(param.getName(), 0);
            nextVarIndex += getTypeSize(param.getType());
        }

        addMethodAnnotations(method, mv);
        addMethodParameterAnnotations(method, mv);

        mv.visitVarInsn(ALOAD, 0); // "this"
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate", proxyClassDescriptor);
        for (int i = 0; i < parameters.length; i++) {
            mv.visitVarInsn(getLoadCode(parameters[i].getType()), paramIndices[i]); // delegate parameters.
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, proxyClassInternalName, methodName, methodDescriptor, false);

        mv.visitInsn(getReturnCode(returnType)); // The actual return entry.

        // Variable table.
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l4, l4, 0);
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            Class<?> type = param.getType();
            mv.visitLocalVariable(param.getName(), Type.getDescriptor(type),
                    TypeUtils.getDescriptorForJavaType(param.getParameterizedType()), l4, l4, paramIndices[i]);
        }

        mv.visitEnd();

    }

    private static void writeInterceptedMethod(Class<?> clazzToProxy, String classToProxyInternalName,
            String classToProxyDescriptor,
            String selfClassInternalName, String selfClassDescriptor, ClassWriter cw,
            MethodVisitor mv, Method method, int methodId) throws IllegalAccessException, InvocationTargetException {
        String methodDescriptor = Type.getMethodDescriptor(method);
        String methodName = method.getName();
        String methodSignature = TypeUtils.getMethodSignature(method);

        String[] exceptionTypes = null;
        Class<?>[] exceptionsClasses = method.getExceptionTypes();
        if (exceptionsClasses.length != 0) {
            exceptionTypes = Stream.of(exceptionsClasses).map(Type::getInternalName).toArray(String[]::new);
        }

        Class<?> returnType = method.getReturnType();

        mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL, methodName, methodDescriptor, methodSignature, exceptionTypes);
        addMethodAnnotations(method, mv);
        addMethodParameterAnnotations(method, mv);
        Parameter[] parameters = method.getParameters();
        int paramCount = parameters.length;

        int[] paramIndices = new int[paramCount];
        int nextVarIndex = 1; // 0 = "this"
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            paramIndices[i] = nextVarIndex;
            mv.visitParameter(param.getName(), 0);
            nextVarIndex += getTypeSize(param.getType());
        }

        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(49, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "interceptor",
                "Lio/primeval/reflect/proxy/Interceptor;");
        mv.visitFieldInsn(GETSTATIC, selfClassInternalName, "cc" + methodId,
                "Lio/primeval/reflect/proxy/CallContext;");

        String interceptionHandlerInternalName = classToProxyInternalName
                + InterceptionHandlerGenerator.SUFFIX_START + method.getName() + methodId;

        mv.visitTypeInsn(NEW, interceptionHandlerInternalName);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate", classToProxyDescriptor);

        String argsClassInternalName = null;
        String argsClassDescriptor = null;
        if (method.getParameterCount() > 0) {
            String argsSuffix = "$argsFor$" + method.getName() + methodId;
            argsClassInternalName = classToProxyInternalName + argsSuffix;
            argsClassDescriptor = ReflectUtils.makeSuffixClassDescriptor(classToProxyDescriptor, argsSuffix);
            mv.visitTypeInsn(NEW, argsClassInternalName);
            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, selfClassInternalName, "cc" + methodId,
                    "Lio/primeval/reflect/proxy/CallContext;");
            mv.visitFieldInsn(GETFIELD, "io/primeval/reflect/proxy/CallContext", "parameters",
                    "Ljava/util/List;");
            for (int i = 0; i < paramCount; i++) {
                mv.visitVarInsn(getLoadCode(parameters[i].getType()), paramIndices[i]); // delegate parameters.
            }
            String constDesc = Type.getMethodDescriptor(Type.VOID_TYPE,
                    Stream.concat(Stream.of(List.class), Stream.of(method.getParameterTypes())).map(Type::getType)
                            .toArray(Type[]::new));
            mv.visitMethodInsn(INVOKESPECIAL, argsClassInternalName, "<init>", constDesc, false);
        }

        mv.visitMethodInsn(INVOKESPECIAL, interceptionHandlerInternalName, "<init>",
                "(" + classToProxyDescriptor + ReflectUtils.nullToEmpty(argsClassDescriptor) + ")V",
                false);

        Class<?> invokeReturnType = returnType.isPrimitive() ? returnType : Object.class;
        String invokeReturnTypeDescriptor = Type.getDescriptor(invokeReturnType);
        Class<?> interceptionHandlerClass = ReflectUtils.getInterceptionHandlerClass(returnType);
        String interceptionHandlerDescriptor = Type.getDescriptor(interceptionHandlerClass);
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/reflect/proxy/Interceptor", "onCall",
                "(Lio/primeval/reflect/proxy/CallContext;" + interceptionHandlerDescriptor
                        + ")" + invokeReturnTypeDescriptor,
                true);
        if (!returnType.isPrimitive()) {
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(returnType));
        }
        mv.visitInsn(getReturnCode(returnType));
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l1, 0);
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            Class<?> type = param.getType();
            mv.visitLocalVariable(param.getName(), Type.getDescriptor(type),
                    TypeUtils.getDescriptorForJavaType(param.getParameterizedType()), l0, l1, paramIndices[i]);
        }
        mv.visitMaxs(-1, -1);
        mv.visitEnd();

    }

    static void visitIntInsn(MethodVisitor mv, int i) {
        switch (i) {
        case -1:
            mv.visitInsn(ICONST_M1);
            break;
        case 0:
            mv.visitInsn(ICONST_0);
            break;
        case 1:
            mv.visitInsn(ICONST_1);
            break;
        case 2:
            mv.visitInsn(ICONST_2);
            break;
        case 3:
            mv.visitInsn(ICONST_3);
            break;
        case 4:
            mv.visitInsn(ICONST_4);
            break;
        case 5:
            mv.visitInsn(ICONST_5);
            break;

        default:
            mv.visitIntInsn(BIPUSH, i);
            break;
        }
    }

}
