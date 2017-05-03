package io.primeval.reflect.proxy.handler.impl;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;

public final class SimpleFloatInterceptionHandler implements FloatInterceptionHandler {

    @FunctionalInterface
    public interface ToFloatFunction<A> {
        float applyAsFloat(A a);
    }

    private final Arguments arguments;
    private final ToFloatFunction<Arguments> invokeFun;

    public SimpleFloatInterceptionHandler(Arguments arguments, ToFloatFunction<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> float invoke(Arguments arguments) throws E {
        return invokeFun.applyAsFloat(arguments);
    }
}
