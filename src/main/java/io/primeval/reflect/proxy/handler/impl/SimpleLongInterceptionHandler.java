package io.primeval.reflect.proxy.handler.impl;

import java.util.function.ToLongFunction;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;

public final class SimpleLongInterceptionHandler implements LongInterceptionHandler {

    private final Arguments arguments;
    private final ToLongFunction<Arguments> invokeFun;

    public SimpleLongInterceptionHandler(Arguments arguments, ToLongFunction<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> long invoke(Arguments arguments) throws E {
        return invokeFun.applyAsLong(arguments);
    }
}
