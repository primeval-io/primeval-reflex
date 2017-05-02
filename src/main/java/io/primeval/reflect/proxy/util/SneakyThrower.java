package io.primeval.reflect.proxy.util;

/**
 * An utility to throw any exception from any method.
 *
 */
public final class SneakyThrower {
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> RuntimeException sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
