package io.primeval.reflect.proxy.composite;

import java.util.function.Predicate;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;

public final class CompositeBooleanInterceptors {

    public static <E extends Throwable> boolean call(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            Predicate<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, createInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, createInterceptionHandler(currentArguments,
                    (args) -> call(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    private static BooleanInterceptionHandler createInterceptionHandler(Arguments arguments,
            Predicate<Arguments> invokeFun) {
        return new BooleanInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return arguments;
            }

            @Override
            public <E extends Throwable> boolean invoke(Arguments arguments) throws E {
                return invokeFun.test(arguments);
            }
        };
    }

}
