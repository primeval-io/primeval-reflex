package io.primeval.reflect.proxy.composite;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;

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
        return call(0, context, handler.getArguments(), handler::invoke);
    }

    public <T, E extends Throwable> T call(int interceptorId, CallContext context, Arguments currentArguments,
            Function<Arguments, T> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createObjectInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createObjectInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, context, args, terminalInvokeFun)));
        }
    }

    private <T> ObjectInterceptionHandler<T> createObjectInterceptionHandler(Arguments arguments,
            Function<Arguments, T> invokeFun) {
        return new ObjectInterceptionHandler<T>() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> T invoke(Arguments arguments) throws E {
                return invokeFun.apply(arguments);
            }
        };
    }

}
