package io.primeval.reflex.proxy.bytecode;

import java.util.HashSet;
import java.util.Set;

public final class BridgingClassLoader extends ClassLoader {
    private static final Set<String> PRIMEVAL_REFLECT_PROXY_PACKAGES = new HashSet<>();
    static {
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.proxy");
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.proxy.bytecode");
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.proxy.shared");
        PRIMEVAL_REFLECT_PROXY_PACKAGES.add("io.primeval.reflect.arguments");
    }

    private final ClassLoader[] classLoaders;

    public BridgingClassLoader(ClassLoader[] classLoaders) {
        this.classLoaders = classLoaders;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        int lastDot = className.lastIndexOf('.');
        String packageName = className.substring(0, lastDot);
        if (PRIMEVAL_REFLECT_PROXY_PACKAGES.contains(packageName)) {
            return BridgingClassLoader.class.getClassLoader().loadClass(className);
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
