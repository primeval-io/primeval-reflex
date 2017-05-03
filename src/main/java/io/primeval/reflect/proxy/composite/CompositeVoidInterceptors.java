package io.primeval.reflect.proxy.composite;

import java.util.function.Consumer;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

public final class CompositeVoidInterceptors {

    public static <E extends Throwable> void call(int interceptorId, Interceptor[] interceptors, CallContext context,
            Arguments currentArguments,
            Consumer<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static VoidInterceptionHandler createInterceptionHandler(Arguments arguments,
            Consumer<Arguments> invokeFun) {
        return new VoidInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> void invoke(Arguments arguments) throws E {
                invokeFun.accept(arguments);
            }
        };
    }

}
