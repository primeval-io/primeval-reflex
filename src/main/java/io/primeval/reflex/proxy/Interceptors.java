package io.primeval.reflex.proxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utilities for {@link Interceptor}.
 */
public final class Interceptors {

    private Interceptors() {
    }

    public static Interceptor stack(Interceptor interceptor) {
        return interceptor;
    }

    public static Interceptor stack(Interceptor... interceptors) {
        if (interceptors.length == 0) {
            return Interceptor.DEFAULT;
        }
        // no defensive copy, trust the client not to share or mutate this
        // array.
        return new StackedInterceptor(interceptors);
    }

    public static Interceptor stack(Iterable<Interceptor> interceptors) {
        Iterator<Interceptor> iterator = interceptors.iterator();
        return stack(iterator);
    }

    public static Interceptor stack(Iterator<Interceptor> interceptors) {
        if (!interceptors.hasNext()) {
            return Interceptor.DEFAULT;
        } else {
            Interceptor first = interceptors.next();

            if (!interceptors.hasNext()) {
                return first;
            }
            List<Interceptor> list = new ArrayList<>();
            list.add(first);
            do {
                list.add(interceptors.next());
            } while (interceptors.hasNext());

            return new StackedInterceptor(list.toArray(new Interceptor[0]));
        }
    }

    public static Interceptor stack(List<Interceptor> interceptors) {
        if (interceptors.isEmpty()) {
            return Interceptor.DEFAULT;
        } else if (interceptors.size() == 1) {
            return interceptors.get(0);
        } else {
            return new StackedInterceptor(interceptors.toArray(new Interceptor[0]));
        }
    }

}
