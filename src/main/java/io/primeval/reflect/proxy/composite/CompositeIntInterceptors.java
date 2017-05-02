package io.primeval.reflect.proxy.composite;

import java.util.function.ToIntFunction;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;

public final class CompositeIntInterceptors {

    public static <T, E extends Throwable> int call(int interceptorId, Interceptor[] interceptors, CallContext context,
            Arguments currentArguments,
            ToIntFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static IntInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToIntFunction<Arguments> invokeFun) {
        return new IntInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> int invoke(Arguments arguments) throws E {
                return invokeFun.applyAsInt(arguments);
            }
        };
    }

}
