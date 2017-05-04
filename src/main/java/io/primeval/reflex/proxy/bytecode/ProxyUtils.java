package io.primeval.reflex.proxy.bytecode;

import java.util.concurrent.Callable;

public final class ProxyUtils {

    // Run any block of code and propagate throwables as necessary
    public static <T> T trust(Callable<T> block) {
        try {
            return block.call();
        } catch (Exception e) {
            throw ProxyUtils.sneakyThrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> RuntimeException sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

}
