package io.primeval.reflect.proxy.bytecode;

import static io.primeval.reflect.proxy.bytecode.ReflectUtils.trust;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ProxyClassLoader extends ClassLoader {
    private final Set<Class<?>> managedClasses = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, Supplier<byte[]>> classesToProxy = new ConcurrentHashMap<>();

    public ProxyClassLoader(ClassLoader parent) {
        super(parent);
    }

    // We can add because we keep 1 classloader per bundle.
    public void declareClassToProxy(Class<?> clazzToProxy, Class<?>[] interfaces, Method[] methods,
            Predicate<Method> shouldIntercept) {
        if (managedClasses.add(clazzToProxy)) {
            String classToProxyName = ProxyClassGenerator.getName(clazzToProxy);
            classesToProxy.put(classToProxyName,
                    () -> trust(() -> ProxyClassGenerator.create(clazzToProxy, interfaces, methods, shouldIntercept)));
            for (int methodId = 0; methodId < methods.length; methodId++) {
                Method m = methods[methodId];
                if (!shouldIntercept.test(m)) {
                    continue;
                }
                int methId = methodId;
                String methodArgName = MethodArgumentsGenerator.getName(clazzToProxy, m, methodId);
                classesToProxy.put(methodArgName,
                        () -> trust(() -> MethodArgumentsGenerator.generateMethodArgs(clazzToProxy, m, methId)));

                String methodArgUpdaterName = MethodArgumentssUpdaterGenerator.getName(clazzToProxy, m, methodId);
                classesToProxy.put(methodArgUpdaterName,
                        () -> trust(() -> MethodArgumentssUpdaterGenerator.generateMethodArgsUpdater(clazzToProxy, m,
                                methId)));

                String methodInterceptionHandlerName = InterceptionHandlerGenerator.getName(
                        clazzToProxy, m,
                        methodId);
                classesToProxy.put(methodInterceptionHandlerName,
                        () -> trust(() -> InterceptionHandlerGenerator
                                .generateMethodInterceptionHandler(clazzToProxy, m,
                                        methId)));
            }
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // We remove, since classes are loaded only once.
        Supplier<byte[]> bytecodeSupplier = classesToProxy.remove(name);
        if (bytecodeSupplier != null) {
            byte[] bytecode = bytecodeSupplier.get();
            Class<?> defineClass = defineClass(name, bytecode, 0, bytecode.length);
            if (resolve) {
                resolveClass(defineClass);
            }
            return defineClass;
        } else {
            return super.loadClass(name, resolve);
        }
    }
}