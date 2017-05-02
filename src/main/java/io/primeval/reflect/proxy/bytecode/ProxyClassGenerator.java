package io.primeval.reflect.proxy.bytecode;

import static io.primeval.reflect.proxy.bytecode.TypeUtils.getBoxed;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getFrameType;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getLoadCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getReturnCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getStoreCode;
import static io.primeval.reflect.proxy.bytecode.TypeUtils.getTypeSize;
import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.DSTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IALOAD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.LSTORE;
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
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import io.primeval.reflect.proxy.bytecode.shared.Proxy;
import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

public final class ProxyClassGenerator {

    public static final String PROXY_TARGET_CLASS_SUFFIX = "$Proxy$";

    public static String getName(Class<?> clazzToWeave) {
        return clazzToWeave.getName() + PROXY_TARGET_CLASS_SUFFIX;
    }

    public static byte[] create(Class<?> clazzToProxy, Class<?>[] interfaces, Method[] methods,
            Predicate<Method> shouldIntercept) throws Exception {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visitSource("@proxy@primeval", null);
        String proxyClassDescriptor = Type.getDescriptor(clazzToProxy);
        String proxyClassInternalName = Type.getInternalName(clazzToProxy);

        String selfClassInternalName = proxyClassInternalName + PROXY_TARGET_CLASS_SUFFIX;
        String selfClassDescriptor = makeTargetClassDescriptor(proxyClassDescriptor);

        Class<?> superclass = Proxy.class;
        // String superClassDescriptor = Type.getDescriptor(superclass);
        String superClassInternalName = Type.getInternalName(superclass);

        String typeSignature = TypeUtils.getTypeSignature(clazzToProxy);
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
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "delegate", proxyClassDescriptor, null, null);
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
            mv.visitLdcInsn(Type.getType(proxyClassDescriptor));
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
            mv.visitMethodInsn(INVOKESTATIC, "io/primeval/reflect/proxy/bytecode/shared/ProxyUtils",
                    "getMethodUnchecked",
                    "(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
            mv.visitFieldInsn(PUTSTATIC, selfClassInternalName, "meth" + i, "Ljava/lang/reflect/Method;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitTypeInsn(NEW, "io/primeval/reflect/proxy/CallContext");
            mv.visitInsn(DUP);
            mv.visitLdcInsn(Type.getType(proxyClassDescriptor));
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

        addTypeAnnotations(clazzToProxy, cw);

        // Constructor
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + proxyClassDescriptor + ")V", null, null);
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
            mv.visitFieldInsn(PUTFIELD, selfClassInternalName, "delegate", proxyClassDescriptor);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", selfClassDescriptor, null, l0, l3, 0);
            mv.visitLocalVariable("delegate", proxyClassDescriptor, null, l0, l3, 1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        // Methods
        for (int methId = 0; methId < methods.length; methId++) {
            Method method = methods[methId];
            if (shouldIntercept.test(method)) {
                writeInterceptedMethod(clazzToProxy, proxyClassInternalName, proxyClassDescriptor,
                        selfClassInternalName,
                        selfClassDescriptor,
                        cw, mv,
                        method, methId);
            } else {
                writeSimpleDelegationMethod(clazzToProxy, proxyClassInternalName, proxyClassDescriptor,
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

    private static void addTypeAnnotations(Class<?> clazzToWeave, ClassWriter cw)
            throws IllegalAccessException, InvocationTargetException {
        for (Annotation ann : clazzToWeave.getDeclaredAnnotations()) {
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

    private static void writeSimpleDelegationMethod(Class<?> clazzToWeave, String proxyClassInternalName,
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

        if (returnType != void.class) {
            mv.visitInsn(getReturnCode(returnType)); // The actual return entry.
        } else {
            mv.visitInsn(RETURN);
        }

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

    private static void writeInterceptedMethod(Class<?> clazzToWeave, String proxyClassInternalName,
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
        String returnTypeDescriptor = Type.getDescriptor(returnType);

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

        int varAdvIndex = nextVarIndex; // Lio/primeval/aspecio/aspect/interceptor/Advice;
        int varCurrentArgsIndex = nextVarIndex + 1; // Lio/primeval/reflect/proxy/arguments/Arguments;
        int varInitialActionIndex = nextVarIndex + 2; // Lio/primeval/aspecio/aspect/interceptor/BeforeAction;

        int varArgumentHookIndex = nextVarIndex + 3; // Lio/primeval/aspecio/aspect/interceptor/Advice$ArgumentHook;
        int varNextActionIndex = nextVarIndex + 4; // Lio/primeval/aspecio/aspect/interceptor/BeforeAction;

        // here var "argumentHook" is descoped, we can reuse the index
        int varReturnValIndex = nextVarIndex + 3; // <returnType>
        int varThrowableIndex = nextVarIndex + 3; // Ljava/lang/Throwable;
        int varResultFinallyIndex = /* max index not used so far */ varReturnValIndex; // <returnType>, but not in local table
        int varThrowableFinallyIndex = varThrowableIndex; // Ljava/lang/Throwable;, but not in the local table

        addMethodAnnotations(method, mv);
        addMethodParameterAnnotations(method, mv);

        mv.visitCode();
        Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
        Label l3 = new Label();
        mv.visitTryCatchBlock(l0, l1, l3, null);
        mv.visitTryCatchBlock(l2, l3, l3, null);
        Label l4 = new Label();
        mv.visitLabel(l4);
        // Advice adv = interceptor.onCall(cc#methId);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "interceptor",
                "Lio/primeval/reflect/proxy/Interceptor;");
        mv.visitFieldInsn(GETSTATIC, selfClassInternalName, "cc" + methId,
                "Lio/primeval/reflect/proxy/CallContext;");

        Class<?> interceptionHandlerClass = getInterceptionHandlerClass(returnType);
        String interceptionHandlerDescriptor = TypeUtils.getDescriptorForJavaType(interceptionHandlerClass);

        // Create a new instance of this method's InterceptionHandler 
        
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/reflect/proxy/Interceptor", "onCall",
                "(Lio/primeval/reflect/proxy/CallContext;" + interceptionHandlerDescriptor
                        + ")" + returnTypeDescriptor,
                true);
        mv.visitVarInsn(ASTORE, varAdvIndex); // var "adv"
        Label l5 = new Label();
        mv.visitLabel(l5);
        // Arguments currentArgs = null;
        mv.visitInsn(ACONST_NULL);
        mv.visitVarInsn(ASTORE, varCurrentArgsIndex); // var "currentArgs"

        // BeforeAction initialAction = adv.initialAction();
        Label l6 = new Label();
        mv.visitLabel(l6);
        mv.visitVarInsn(ALOAD, varAdvIndex); // var "adv"
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice", "initialAction",
                "()Lio/primeval/aspecio/aspect/interceptor/BeforeAction;", true);
        mv.visitVarInsn(ASTORE, varInitialActionIndex); // var "initialAction"
        Label l7 = new Label();
        mv.visitLabel(l7);
        // switch (initialAction) {

        mv.visitMethodInsn(INVOKESTATIC, selfClassInternalName,
                "$SWITCH_TABLE$io$primeval$aspecio$aspect$interceptor$BeforeAction",
                "()[I", false);
        Label l8 = new Label();
        mv.visitLabel(l8);
        mv.visitVarInsn(ALOAD, varInitialActionIndex); // var "initialAction"
        mv.visitMethodInsn(INVOKEVIRTUAL, "io/primeval/aspecio/aspect/interceptor/BeforeAction", "ordinal", "()I",
                false);
        mv.visitInsn(IALOAD);

        // case SKIP_AND_RETURN: {
        // return ((SkipCall) adv).skipCallAndReturnObject();
        // }

        Label l9 = new Label();
        Label l10 = new Label();
        mv.visitTableSwitchInsn(1, 2, l0, new Label[] { l9, l10 });
        mv.visitLabel(l9);
        mv.visitFrame(
                Opcodes.F_APPEND, 3, new Object[] { "io/primeval/aspecio/aspect/interceptor/Advice",
                        "io/primeval/reflect/proxy/arguments/Arguments",
                        "io/primeval/aspecio/aspect/interceptor/BeforeAction" },
                0, null);
        mv.visitVarInsn(ALOAD, varAdvIndex); // var "adv"
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall");

        skipAndReturnCall(mv, returnType);

        // case REQUEST_ARGUMENTS: {
        // Advice.ArgumentHook argumentHook = (ArgumentHook) adv;
        // if (currentArgs == null) {
        // currentArgs = Arguments.EMPTY_ARGUMENTS OR dynamic arguments.
        // }
        mv.visitLabel(l10);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, varAdvIndex); // var adv
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$ArgumentHook");
        mv.visitVarInsn(ASTORE, varArgumentHookIndex); // var "argumentHook"
        Label l12 = new Label();
        mv.visitLabel(l12);
        mv.visitVarInsn(ALOAD, varCurrentArgsIndex); // var "currentArgs"
        Label l13 = new Label();
        mv.visitJumpInsn(IFNONNULL, l13);
        Label l14 = new Label();
        mv.visitLabel(l14);
        String argsClassInternalName = null;
        if (method.getParameterCount() == 0) {
            mv.visitFieldInsn(GETSTATIC, "io/primeval/reflect/proxy/arguments/Arguments",
                    "EMPTY_ARGUMENTS",
                    "Lio/primeval/reflect/proxy/arguments/Arguments;");
        } else {
            argsClassInternalName = proxyClassInternalName + "$argsFor$" + method.getName() + methId;
            mv.visitTypeInsn(NEW, argsClassInternalName);
            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, selfClassInternalName, "cc" + methId,
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
        mv.visitVarInsn(ASTORE, varCurrentArgsIndex); // var "currentArgs"

        // BeforeAction nextAction = argumentHook.visitArguments(currentArgs);
        // switch (nextAction) {

        mv.visitLabel(l13);
        mv.visitFrame(Opcodes.F_APPEND, 1,
                new Object[] { "io/primeval/aspecio/aspect/interceptor/Advice$ArgumentHook" }, 0, null);
        mv.visitVarInsn(ALOAD, varArgumentHookIndex); // var "argumentHook"
        mv.visitVarInsn(ALOAD, varCurrentArgsIndex); // var "currentArgs"
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$ArgumentHook",
                "visitArguments",
                "(Lio/primeval/reflect/proxy/arguments/Arguments;)Lio/primeval/aspecio/aspect/interceptor/BeforeAction;",
                true);
        mv.visitVarInsn(ASTORE, varNextActionIndex); // var "nextAction"

        // case SKIP_AND_RETURN:
        // return ((SkipCall) adv).skipCallAndReturnXXX();
        Label l15 = new Label();
        mv.visitLabel(l15);
        mv.visitMethodInsn(INVOKESTATIC, selfClassInternalName,
                "$SWITCH_TABLE$io$primeval$aspecio$aspect$interceptor$BeforeAction",
                "()[I", false);
        Label l16 = new Label();
        mv.visitLabel(l16);
        mv.visitVarInsn(ALOAD, varNextActionIndex); // var "nextAction"
        mv.visitMethodInsn(INVOKEVIRTUAL, "io/primeval/aspecio/aspect/interceptor/BeforeAction", "ordinal", "()I",
                false);
        mv.visitInsn(IALOAD);
        Label l17 = new Label();
        Label l18 = new Label();
        mv.visitTableSwitchInsn(1, 3, l0, new Label[] { l17, l0, l18 });
        mv.visitLabel(l17);
        mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "io/primeval/aspecio/aspect/interceptor/BeforeAction" }, 0,
                null);
        mv.visitVarInsn(ALOAD, varAdvIndex); // var "adv"
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall");

        skipAndReturnCall(mv, returnType);

        // case UPDATE_ARGUMENTS_AND_PROCEED:
        // currentArgs = argumentHook.updateArguments(currentArgs);
        // break;
        mv.visitLabel(l18);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, varArgumentHookIndex); // var "argumentHook"
        mv.visitVarInsn(ALOAD, varCurrentArgsIndex); // var "currentArgs"
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$ArgumentHook",
                "updateArguments",
                "(Lio/primeval/reflect/proxy/arguments/Arguments;)Lio/primeval/reflect/proxy/arguments/Arguments;",
                true);
        // mv.visitTypeInsn(CHECKCAST, argsClassInternalName); // this is implicit
        mv.visitVarInsn(ASTORE, varCurrentArgsIndex);// var "currentArgs"

        // default:
        // break;
        // }
        // }
        // default:
        // break;
        // }
        Label l20 = new Label();
        mv.visitLabel(l20);
        mv.visitJumpInsn(GOTO, l0); // break

        mv.visitLabel(l0);

        mv.visitFrame(Opcodes.F_CHOP, 2, null, 0, null);

        // if (currentArgs != null) {
        // arg0 = currentArgs.arg0;
        // arg1 = currentArgs.arg1;
        // ...
        // }
        if (paramCount > 0) {
            mv.visitVarInsn(ALOAD, varCurrentArgsIndex);
            Label makeCall = new Label();
            mv.visitJumpInsn(IFNULL, makeCall);
            assert argsClassInternalName != null;
            // prepare value
            mv.visitVarInsn(ALOAD, varCurrentArgsIndex);
            mv.visitTypeInsn(CHECKCAST, argsClassInternalName);
            for (int i = 0; i < paramCount; i++) {
                Parameter parameter = parameters[i];
                Class<?> paramType = parameter.getType();
                if (i + 1 < paramCount) { // if not last
                    mv.visitInsn(DUP); // don't pay for checkcast N times.
                }
                mv.visitFieldInsn(GETFIELD, argsClassInternalName, parameter.getName(), Type.getDescriptor(paramType));
                mv.visitVarInsn(getStoreCode(paramType), paramIndices[i]);
            }

            mv.visitLabel(makeCall);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }

        // !\\ THIS IS THE DELEGATION CALL //!\\

        mv.visitVarInsn(ALOAD, 0); // "this"
        mv.visitFieldInsn(GETFIELD, selfClassInternalName, "delegate", proxyClassDescriptor);
        for (int i = 0; i < parameters.length; i++) {
            mv.visitVarInsn(getLoadCode(parameters[i].getType()), paramIndices[i]); // delegate parameters.
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, proxyClassInternalName, methodName, methodDescriptor, false);

        if (returnType != void.class) {
            mv.visitVarInsn(getStoreCode(returnType), varReturnValIndex); // var "returnVal"
        }

        // !\\ WE ARE DONE :-) //!\\

        // if ((this.adv.hasPhase(Advice.CallReturn.PHASE))) {
        // returnVal = ((CallReturn) adv).onXXXXReturn(returnVal);
        // }
        // return returnVal;
        Label l21 = new Label();
        mv.visitLabel(l21);
        mv.visitVarInsn(ALOAD, varAdvIndex); // var "adv"
        mv.visitInsn(ICONST_1); // CallReturn Phase (hardcompiled).
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice", "hasPhase", "(I)Z", true);
        Label l22 = new Label();
        mv.visitJumpInsn(IFEQ, l22);
        Label l23 = new Label();
        mv.visitLabel(l23);
        mv.visitVarInsn(ALOAD, varAdvIndex); // var "adv"
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn");

        if (returnType != void.class) {
            mv.visitVarInsn(getLoadCode(returnType), varReturnValIndex); // var "returnVal"
        }
        callOnReturnAndStoreReturnIfPossible(mv, returnType, varReturnValIndex);

        mv.visitLabel(l22);

        if (returnType == void.class) {
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        } else {
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { getFrameType(returnType) }, 0, null);
            mv.visitVarInsn(getLoadCode(returnType), varReturnValIndex); // var "returnVal"
            mv.visitVarInsn(getStoreCode(returnType), varResultFinallyIndex); // Store the result in an unnamed local variable for the
                                                                              // finally
            // block's return.
        }
        // Finally blocks are inlined at each exit, here's one!
        // That's why we don't return here!

        // } finally {
        // if ((adv.hasPhase(Advice.Finally.PHASE))) {
        // ((Advice.Finally) adv).runFinally();
        // }
        // }

        mv.visitLabel(l1);
        mv.visitVarInsn(ALOAD, varAdvIndex); // variable "adv"
        mv.visitInsn(ICONST_4); // Finally PHASE (hard compiled) = 4
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice", "hasPhase", "(I)Z", true);

        // <if block>
        Label l25 = new Label();
        mv.visitJumpInsn(IFEQ, l25);
        Label l26 = new Label();
        mv.visitLabel(l26);
        mv.visitVarInsn(ALOAD, varAdvIndex); // variable "adv"
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$Finally");

        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$Finally", "runFinally",
                "()V", true);
        mv.visitLabel(l25);
        // </if block>

        visitFullFrame1(selfClassInternalName, parameters, returnType, mv);

        if (returnType != void.class) {
            mv.visitVarInsn(getLoadCode(returnType), varResultFinallyIndex); // retrieve the actual result, stored earlier in that unnamed
            // variable.
            mv.visitInsn(getReturnCode(returnType)); // The actual return entry.
        } else {
            mv.visitInsn(RETURN);
        }

        visitFullFrame2(selfClassInternalName, parameters, returnType, mv);

        // } catch (Throwable throwable) {
        // if ((adv.hasPhase(Advice.Catch.PHASE))) {
        // throwable = ((Advice.Catch) adv).reThrow(throwable);
        // }
        // throw newChecked();
        // }
        mv.visitLabel(l2);
        // postThrowVisitFrame(selfClassInternalName, mv);
        mv.visitVarInsn(ASTORE, varThrowableIndex); // var "throwable"

        Label l28 = new Label();
        mv.visitLabel(l28);
        mv.visitVarInsn(ALOAD, varAdvIndex); // variable "adv"
        mv.visitInsn(ICONST_2); // Catch phase (hard compiled = 2)
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice", "hasPhase", "(I)Z", true);
        Label l29 = new Label();
        mv.visitJumpInsn(IFEQ, l29);
        Label l30 = new Label();
        mv.visitLabel(l30);
        mv.visitVarInsn(ALOAD, varAdvIndex); // variable "adv"
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$Catch");
        mv.visitVarInsn(ALOAD, varThrowableIndex); // variable "throwable"
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$Catch", "reThrow",
                "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", true);
        mv.visitVarInsn(ASTORE, varThrowableIndex); // variable
                                                    // "throwable"
        mv.visitLabel(l29);
        mv.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "java/lang/Throwable" }, 0, null);
        mv.visitVarInsn(ALOAD, varThrowableIndex); // variable
                                                   // "throwable"
        mv.visitInsn(ATHROW);
        mv.visitLabel(l3);
        postThrowVisitFrame(selfClassInternalName, parameters, mv);
        mv.visitVarInsn(ASTORE, varThrowableFinallyIndex); // store the throwable for the inlined finally block
        // That's also why we don't actually throw here!

        // Finally blocks are inlined at each exit, here's the other one!
        // That's why we don't return here!

        // } finally {
        // if ((adv.hasPhase(Advice.Finally.PHASE))) {
        // ((Advice.Finally) adv).runFinally();
        // }
        // }

        Label l32 = new Label();
        mv.visitLabel(l32);
        mv.visitVarInsn(ALOAD, varAdvIndex); // variable "adv"
        mv.visitInsn(ICONST_4);
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice", "hasPhase", "(I)Z", true);
        Label l33 = new Label();
        mv.visitJumpInsn(IFEQ, l33);
        Label l34 = new Label();
        mv.visitLabel(l34);
        mv.visitVarInsn(ALOAD, varAdvIndex); // variable "adv"
        mv.visitTypeInsn(CHECKCAST, "io/primeval/aspecio/aspect/interceptor/Advice$Finally");
        mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$Finally", "runFinally",
                "()V", true);
        mv.visitLabel(l33);

        lastVisitFrame(selfClassInternalName, parameters, mv);

        mv.visitVarInsn(ALOAD, varThrowableFinallyIndex); // get the stored throwable back
        mv.visitInsn(ATHROW); // throw it

        // Variable table.
        Label l36 = new Label();
        mv.visitLabel(l36);
        mv.visitLocalVariable("this", selfClassDescriptor, null, l4, l36, 0);
        for (int i = 0; i < paramCount; i++) {
            Parameter param = parameters[i];
            Class<?> type = param.getType();
            mv.visitLocalVariable(param.getName(), Type.getDescriptor(type),
                    TypeUtils.getDescriptorForJavaType(param.getParameterizedType()), l4, l36, paramIndices[i]);
        }
        mv.visitLocalVariable("adv", "Lio/primeval/aspecio/aspect/interceptor/Advice;", null, l5, l36, varAdvIndex);
        mv.visitLocalVariable("currentArgs", "Lio/primeval/reflect/proxy/arguments/Arguments;", null, l6,
                l36, varCurrentArgsIndex);
        mv.visitLocalVariable("initialAction", "Lio/primeval/aspecio/aspect/interceptor/BeforeAction;", null, l7, l36,
                varInitialActionIndex);
        mv.visitLocalVariable("argumentHook", "Lio/primeval/aspecio/aspect/interceptor/Advice$ArgumentHook;", null, l12,
                l0,
                varArgumentHookIndex);
        mv.visitLocalVariable("nextAction", "Lio/primeval/aspecio/aspect/interceptor/BeforeAction;", null, l15, l0,
                varNextActionIndex);
        mv.visitLocalVariable("returnVal", "Ljava/lang/String;", null, l21, l2, varReturnValIndex);
        mv.visitLocalVariable("throwable", "Ljava/lang/Throwable;", null, l28, l3, varThrowableIndex);
        mv.visitMaxs(-1, -1);
        mv.visitEnd();

    }

    private static Class<?> getInterceptionHandlerClass(Class<?> returnType) {
        if (returnType == void.class) {
            return VoidInterceptionHandler.class;
        } else if (returnType == int.class) {
            return IntInterceptionHandler.class;
        } else if (returnType == short.class) {
            return ShortInterceptionHandler.class;
        } else if (returnType == double.class) {
            return DoubleInterceptionHandler.class;
        } else if (returnType == float.class) {
            return FloatInterceptionHandler.class;
        } else if (returnType == char.class) {
            return CharInterceptionHandler.class;
        } else if (returnType == long.class) {
            return LongInterceptionHandler.class;
        } else if (returnType == byte.class) {
            return ByteInterceptionHandler.class;
        } else if (returnType == boolean.class) {
            return BooleanInterceptionHandler.class;
        } else {
            return ObjectInterceptionHandler.class;
        }
    }

    private static void callOnReturnAndStoreReturnIfPossible(MethodVisitor mv, Class<?> returnType,
            int varReturnValIndex) {
        if (returnType == void.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onVoidReturn",
                    "()V", true);
            // we don't have a returnVal on void cases :)
        } else if (returnType == int.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onIntReturn",
                    "(I)I", true);
            mv.visitVarInsn(ISTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == short.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onShortReturn",
                    "(S)S", true);
            mv.visitVarInsn(ISTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == double.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onDoubleReturn",
                    "(D)D", true);
            mv.visitVarInsn(DSTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == float.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onFloatReturn",
                    "(F)F", true);
            mv.visitVarInsn(FSTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == char.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onCharReturn",
                    "(C)C", true);
            mv.visitVarInsn(ISTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == long.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onLongReturn",
                    "(J)J", true);
            mv.visitVarInsn(LSTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == byte.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onByteReturn",
                    "(B)B", true);
            mv.visitVarInsn(ISTORE, varReturnValIndex); // var "returnVal"
        } else if (returnType == boolean.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onBooleanReturn",
                    "(Z)Z", true);
            mv.visitVarInsn(ISTORE, varReturnValIndex); // var "returnVal"
        } else {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$CallReturn",
                    "onObjectReturn",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(returnType));
            mv.visitVarInsn(ASTORE, varReturnValIndex); // var "returnVal"
        }
    }

    private static void visitFullFrame1(String selfClassInternalName, Parameter[] parameters, Class<?> returnType,
            MethodVisitor mv) {

        if (returnType == void.class) {
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }

        else {
            Object[] locals = new Object[4 + parameters.length + 1];
            locals[0] = selfClassInternalName;
            int idx = 1;
            for (int i = 0; i < parameters.length; i++) {
                locals[idx++] = getFrameType(parameters[i].getType());
            }
            locals[idx++] = "io/primeval/aspecio/aspect/interceptor/Advice";
            locals[idx++] = "io/primeval/reflect/proxy/arguments/Arguments";
            locals[idx++] = "io/primeval/aspecio/aspect/interceptor/BeforeAction";
            locals[idx++] = getFrameType(returnType);

            mv.visitFrame(Opcodes.F_FULL, locals.length, locals, 0, new Object[] {});

        }
    }

    private static void visitFullFrame2(String selfClassInternalName, Parameter[] parameters, Class<?> returnType,
            MethodVisitor mv) {
        if (returnType == void.class) {
            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "java/lang/Throwable" });
        }

        else {
            Object[] locals = new Object[4 + parameters.length];
            locals[0] = selfClassInternalName;
            int idx = 1;
            for (int i = 0; i < parameters.length; i++) {
                locals[idx++] = getFrameType(parameters[i].getType());
            }
            locals[idx++] = "io/primeval/aspecio/aspect/interceptor/Advice";
            locals[idx++] = "io/primeval/reflect/proxy/arguments/Arguments";
            locals[idx++] = "io/primeval/aspecio/aspect/interceptor/BeforeAction";

            mv.visitFrame(Opcodes.F_FULL, locals.length, locals, 1, new Object[] { "java/lang/Throwable" });

        }

    }

    private static void postThrowVisitFrame(String selfClassInternalName, Parameter[] parameters, MethodVisitor mv) {
        Object[] locals = new Object[4 + parameters.length];
        locals[0] = selfClassInternalName;
        int idx = 1;
        for (int i = 0; i < parameters.length; i++) {
            locals[idx++] = getFrameType(parameters[i].getType());
        }
        locals[idx++] = "io/primeval/aspecio/aspect/interceptor/Advice";
        locals[idx++] = "io/primeval/reflect/proxy/arguments/Arguments";
        locals[idx++] = "io/primeval/aspecio/aspect/interceptor/BeforeAction";
        mv.visitFrame(Opcodes.F_FULL, locals.length, locals, 1, new Object[] { "java/lang/Throwable" });
    }

    private static void lastVisitFrame(String selfClassInternalName, Parameter[] parameters, MethodVisitor mv) {
        Object[] locals = new Object[4 + parameters.length + 1];
        locals[0] = selfClassInternalName;
        int idx = 1;
        for (int i = 0; i < parameters.length; i++) {
            locals[idx++] = getFrameType(parameters[i].getType());
        }
        locals[idx++] = "io/primeval/aspecio/aspect/interceptor/Advice";
        locals[idx++] = "io/primeval/reflect/proxy/arguments/Arguments";
        locals[idx++] = "io/primeval/aspecio/aspect/interceptor/BeforeAction";
        // locals[idx++] = Opcodes.TOP;
        // locals[idx++] = Opcodes.TOP;
        locals[idx++] = "java/lang/Throwable";
        mv.visitFrame(Opcodes.F_FULL, locals.length, locals, 0, new Object[0]);
    }

    private static void skipAndReturnCall(MethodVisitor mv, Class<?> returnType) {
        if (returnType == void.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnVoid",
                    "()V", true);
            mv.visitInsn(RETURN);
        } else if (returnType == int.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnInt",
                    "()I", true);
            mv.visitInsn(IRETURN);
        } else if (returnType == short.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnShort",
                    "()S", true);
            mv.visitInsn(IRETURN);

        } else if (returnType == double.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnDouble",
                    "()D", true);
            mv.visitInsn(DRETURN);
        } else if (returnType == float.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnFloat",
                    "()F", true);
            mv.visitInsn(FRETURN);

        } else if (returnType == char.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnChar",
                    "()C", true);
            mv.visitInsn(IRETURN);

        } else if (returnType == long.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnLong",
                    "()J", true);
            mv.visitInsn(LRETURN);
        } else if (returnType == byte.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnByte",
                    "()B", true);
            mv.visitInsn(IRETURN);
        } else if (returnType == boolean.class) {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnBoolean",
                    "()Z", true);
            mv.visitInsn(IRETURN);
        } else {
            mv.visitMethodInsn(INVOKEINTERFACE, "io/primeval/aspecio/aspect/interceptor/Advice$SkipCall",
                    "skipCallAndReturnObject",
                    "()Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(returnType));
            mv.visitInsn(ARETURN);
        }
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
