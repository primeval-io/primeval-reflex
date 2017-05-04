package io.primeval.reflex.proxy.handler.impl;

import java.util.function.ToDoubleFunction;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.DoubleInterceptionHandler;

public final class SimpleDoubleInterceptionHandler implements DoubleInterceptionHandler {

    private final Arguments arguments;
    private final ToDoubleFunction<Arguments> invokeFun;

    public SimpleDoubleInterceptionHandler(Arguments arguments, ToDoubleFunction<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> double invoke(Arguments arguments) throws E {
        return invokeFun.applyAsDouble(arguments);
    }
}
