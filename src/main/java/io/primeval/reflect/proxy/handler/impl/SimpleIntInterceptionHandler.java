package io.primeval.reflect.proxy.handler.impl;

import java.util.function.ToIntFunction;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;

public final class SimpleIntInterceptionHandler implements IntInterceptionHandler {

    private final Arguments arguments;
    private final ToIntFunction<Arguments> invokeFun;

    public SimpleIntInterceptionHandler(Arguments arguments, ToIntFunction<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> int invoke(Arguments arguments) throws E {
        return invokeFun.applyAsInt(arguments);
    }
}
