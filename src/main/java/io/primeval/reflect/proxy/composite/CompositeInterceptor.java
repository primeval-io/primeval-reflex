package io.primeval.reflect.proxy.composite;

import java.util.Iterator;
import java.util.stream.Stream;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
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

/**
 * An Interceptor composed of several Interceptors. Build with {@link Interceptors#compose(Interceptor...)}.
 */
final class CompositeInterceptor implements Interceptor {

    private final Interceptor[] interceptors;
    private String repr;

    public CompositeInterceptor(Interceptor[] interceptors) {
        if (interceptors == null || interceptors.length == 0) {
            throw new IllegalArgumentException("interceptors must be non-empty");
        }
        this.interceptors = interceptors;
    }

    @Override
    public String toString() {
        if (repr == null) {
            repr = new StringBuilder().append("Composite{").append(String.join(",", new Iterable<String>() {
                public Iterator<String> iterator() {
                    return Stream.of(interceptors).map(Object::toString).iterator();
                }
            })).append('}').toString();
        }
        return repr;

    }

    @Override
    public <T, E extends Throwable> T onCall(CallContext context, ObjectInterceptionHandler<T> handler) throws E {
        return CompositeObjectInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> void onCall(CallContext context, VoidInterceptionHandler handler) throws E {
        CompositeVoidInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> int onCall(CallContext context, IntInterceptionHandler handler) throws E {
        return CompositeIntInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> byte onCall(CallContext context, ByteInterceptionHandler handler) throws E {
        return CompositeByteInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> boolean onCall(CallContext context, BooleanInterceptionHandler handler) throws E {
        return CompositeBooleanInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> short onCall(CallContext context, ShortInterceptionHandler handler) throws E {
        return CompositeShortInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> char onCall(CallContext context, CharInterceptionHandler handler) throws E {
        return CompositeCharInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> double onCall(CallContext context, DoubleInterceptionHandler handler) throws E {
        return CompositeDoubleInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> float onCall(CallContext context, FloatInterceptionHandler handler) throws E {
        return CompositeFloatInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    @Override
    public <E extends Throwable> long onCall(CallContext context, LongInterceptionHandler handler) throws E {
        return CompositeLongInterceptors.call(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

}
