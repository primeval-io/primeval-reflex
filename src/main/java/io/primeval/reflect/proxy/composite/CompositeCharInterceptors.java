package io.primeval.reflect.proxy.composite;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;

public final class CompositeCharInterceptors {

    @FunctionalInterface
    public interface ToCharFunction<A> {
        char applyAsChar(A a);
    }

    public static <E extends Throwable> char call(int interceptorId, Interceptor[] interceptors, CallContext context,
            Arguments currentArguments,
            ToCharFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static CharInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToCharFunction<Arguments> invokeFun) {
        return new CharInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> char invoke(Arguments arguments) throws E {
                return invokeFun.applyAsChar(arguments);
            }
        };
    }

}
