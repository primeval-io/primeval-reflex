package io.primeval.reflect.proxy.shared;

import java.lang.reflect.Method;

// Class meant to be used *from* the proxy classes.
public final class ProxyUtils {

    private ProxyUtils() {
    }

    public static Method getMethodUnchecked(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            return clazz.getMethod(methodName, params);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new AssertionError("Inconsistent proxy");
        }
    }

}
