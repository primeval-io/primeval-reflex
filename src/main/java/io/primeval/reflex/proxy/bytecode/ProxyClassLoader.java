package io.primeval.reflex.proxy.bytecode;

import static io.primeval.reflex.proxy.bytecode.ProxyUtils.trust;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

import io.primeval.reflex.proxy.bytecode.gen.InterceptionHandlerGenerator;
import io.primeval.reflex.proxy.bytecode.gen.MethodArgumentsGenerator;
import io.primeval.reflex.proxy.bytecode.gen.MethodArgumentssUpdaterGenerator;
import io.primeval.reflex.proxy.bytecode.gen.ProxyClassGenerator;

public final class ProxyClassLoader extends ClassLoader {

    private final Set<Class<?>> managedClasses = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, Supplier<ClassDef>> classesToProxy = new ConcurrentHashMap<>();

    public ProxyClassLoader(ClassLoader parent) {
        super(parent);
    }

    // We can add because we keep 1 classloader per bundle.
    public void declareClassToProxy(Class<?> clazzToProxy, Class<?>[] interfaces, Method[] methods,
            Predicate<Method> shouldIntercept) {
        if (managedClasses.add(clazzToProxy)) {
            String classToProxyName = ProxyClassGenerator.getName(clazzToProxy);
            classesToProxy.put(classToProxyName,
                    () -> trust(() -> ClassDef.from(
                            ProxyClassGenerator.create(clazzToProxy, interfaces, methods, shouldIntercept),
                            clazzToProxy.getProtectionDomain())));
            for (int methodId = 0; methodId < methods.length; methodId++) {
                Method m = methods[methodId];
                if (!shouldIntercept.test(m)) {
                    continue;
                }
                int methId = methodId;
                String methodArgName = MethodArgumentsGenerator.getName(clazzToProxy, m, methodId);
                classesToProxy.put(methodArgName,
                        () -> trust(() -> ClassDef
                                .from(MethodArgumentsGenerator.generateMethodArgs(clazzToProxy, m, methId),
                                        clazzToProxy.getProtectionDomain())));

                String methodArgUpdaterName = MethodArgumentssUpdaterGenerator.getName(clazzToProxy, m, methodId);
                classesToProxy.put(methodArgUpdaterName,
                        () -> trust(() -> ClassDef
                                .from(MethodArgumentssUpdaterGenerator.generateMethodArgsUpdater(clazzToProxy, m,
                                        methId), clazzToProxy.getProtectionDomain())));

                String methodInterceptionHandlerName = InterceptionHandlerGenerator.getName(
                        clazzToProxy, m,
                        methodId);
                classesToProxy.put(methodInterceptionHandlerName,
                        () -> trust(() -> ClassDef.from(InterceptionHandlerGenerator
                                .generateMethodInterceptionHandler(clazzToProxy, m,
                                        methId),
                                clazzToProxy.getProtectionDomain())));
            }
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // We remove, since classes are loaded only once.
        Supplier<ClassDef> bytecodeSupplier = classesToProxy.remove(name);
        if (bytecodeSupplier != null) {
            ClassDef classDef = bytecodeSupplier.get();
            byte[] bytecode = classDef.bytecode;
            Class<?> defineClass = defineClass(name, bytecode, 0, bytecode.length, classDef.pd);
            if (resolve) {
                resolveClass(defineClass);
            }
            return defineClass;
        } else {
            return super.loadClass(name, resolve);
        }
    }

}

final class ClassDef {
    public final byte[] bytecode;
    public final ProtectionDomain pd;

    private ClassDef(byte[] bytecode, ProtectionDomain pd) {
        super();
        this.bytecode = bytecode;
        this.pd = pd;
    }

    public static ClassDef from(byte[] bytecode, ProtectionDomain pd) {
        return new ClassDef(bytecode, pd);
    }

}