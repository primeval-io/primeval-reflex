package io.primeval.reflex.proxy.handler.impl;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.ShortInterceptionHandler;

public final class SimpleShortInterceptionHandler implements ShortInterceptionHandler {

    @FunctionalInterface
    public interface ToShortFunction<A> {
        short applyAsShort(A a);
    }

    private final Arguments arguments;
    private final ToShortFunction<Arguments> invokeFun;

    public SimpleShortInterceptionHandler(Arguments arguments, ToShortFunction<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> short invoke(Arguments arguments) throws E {
        return invokeFun.applyAsShort(arguments);
    }
}
