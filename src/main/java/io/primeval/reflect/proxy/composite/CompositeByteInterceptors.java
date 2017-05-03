package io.primeval.reflect.proxy.composite;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;

public final class CompositeByteInterceptors {

    @FunctionalInterface
    public interface ToByteFunction<A> {
        byte applyAsByte(A a);
    }

    public static <E extends Throwable> byte call(int interceptorId, Interceptor[] interceptors, CallContext context,
            Arguments currentArguments,
            ToByteFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static ByteInterceptionHandler createInterceptionHandler(Arguments arguments,
            ToByteFunction<Arguments> invokeFun) {
        return new ByteInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> byte invoke(Arguments arguments) throws E {
                return invokeFun.applyAsByte(arguments);
            }
        };
    }

}
