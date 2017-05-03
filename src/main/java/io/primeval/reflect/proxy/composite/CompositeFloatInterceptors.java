package io.primeval.reflect.proxy.composite;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;

public final class CompositeFloatInterceptors {

    @FunctionalInterface
    public interface ToFloatFunction<A> {
        float applyAsFloat(A a);
    }

    public static <E extends Throwable> float call(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            ToFloatFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static FloatInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToFloatFunction<Arguments> invokeFun) {
        return new FloatInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> float invoke(Arguments arguments) throws E {
                return invokeFun.applyAsFloat(arguments);
            }
        };
    }

}
