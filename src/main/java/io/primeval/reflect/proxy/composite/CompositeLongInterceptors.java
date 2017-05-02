package io.primeval.reflect.proxy.composite;

import java.util.function.ToLongFunction;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;

public final class CompositeLongInterceptors {

    public static <E extends Throwable> long call(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            ToLongFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static LongInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToLongFunction<Arguments> invokeFun) {
        return new LongInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> long invoke(Arguments arguments) throws E {
                return invokeFun.applyAsLong(arguments);
            }
        };
    }

}
