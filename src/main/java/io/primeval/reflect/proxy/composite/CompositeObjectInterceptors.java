package io.primeval.reflect.proxy.composite;

import java.util.function.Function;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;

public final class CompositeObjectInterceptors {

    public static <T, E extends Throwable> T call(int interceptorId, Interceptor[] interceptors, CallContext context,
            Arguments currentArguments,
            Function<Arguments, T> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static <T> ObjectInterceptionHandler<T> createInterceptionHandler(Arguments arguments,
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
