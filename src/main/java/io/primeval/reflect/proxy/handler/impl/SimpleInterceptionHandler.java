package io.primeval.reflect.proxy.handler.impl;

import java.util.function.Function;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.handler.InterceptionHandler;

public final class SimpleInterceptionHandler<T> implements InterceptionHandler<T> {

    private final Arguments arguments;
    private final Function<Arguments, T> invokeFun;

    public SimpleInterceptionHandler(Arguments arguments, Function<Arguments, T> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> T invoke(Arguments arguments) throws E {
        return invokeFun.apply(arguments);
    }
}
