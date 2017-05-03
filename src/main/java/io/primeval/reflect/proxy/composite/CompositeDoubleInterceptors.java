package io.primeval.reflect.proxy.composite;

import java.util.function.ToDoubleFunction;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;

public final class CompositeDoubleInterceptors {

    public static <E extends Throwable> double call(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            ToDoubleFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static DoubleInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToDoubleFunction<Arguments> invokeFun) {
        return new DoubleInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> double invoke(Arguments arguments) throws E {
                return invokeFun.applyAsDouble(arguments);
            }
        };
    }

}
