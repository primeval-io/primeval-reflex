package io.primeval.reflex.proxy.handler.impl;

import java.util.function.ToIntFunction;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.IntInterceptionHandler;

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
