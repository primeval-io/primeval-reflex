package io.primeval.reflect.proxy.composite;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;

public final class CompositeShortInterceptors {

    @FunctionalInterface
    public interface ToShortFunction<A> {
        short applyAsShort(A a);
    }

    public static <T, E extends Throwable> short call(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            ToShortFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static ShortInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToShortFunction<Arguments> invokeFun) {
        return new ShortInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> short invoke(Arguments arguments) throws E {
                return invokeFun.applyAsShort(arguments);
            }
        };
    }

}
