package io.primeval.reflect.proxy.bytecode;

import java.util.HashSet;
import java.util.Set;

public final class BridgingClassLoader extends ClassLoader {
    private static final Set<String> PRIMEVAL_REFLECT_PROXY_PACKAGES = new HashSet<>();
    static {
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.proxy");
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.arguments");
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.proxy.shared");
    }

    private final ClassLoader primevalReflectClassLoader;
    private final ClassLoader[] classLoaders;

    public BridgingClassLoader(ClassLoader[] classLoaders, ClassLoader primevalReflectClassLoader) {
        this.classLoaders = classLoaders;
        this.primevalReflectClassLoader = primevalReflectClassLoader;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        int lastDot = className.lastIndexOf('.');
        String packageName = className.substring(0, lastDot);
        if (PRIMEVAL_REFLECT_PROXY_PACKAGES.contains(packageName)) {
            return primevalReflectClassLoader.loadClass(className);
        }

        for (int i = 0; i < classLoaders.length; i++) {
            try {
                return classLoaders[i].loadClass(className);
            } catch (ClassNotFoundException cnfe) {
                // continue
            }
        }

        throw new ClassNotFoundException(className);
    }

}
