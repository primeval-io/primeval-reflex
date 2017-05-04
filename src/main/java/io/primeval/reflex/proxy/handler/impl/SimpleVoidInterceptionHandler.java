package io.primeval.reflex.proxy.handler.impl;

import java.util.function.Consumer;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.VoidInterceptionHandler;

public final class SimpleVoidInterceptionHandler implements VoidInterceptionHandler {

    private final Arguments arguments;
    private final Consumer<Arguments> invokeFun;

    public SimpleVoidInterceptionHandler(Arguments arguments, Consumer<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> void invoke(Arguments arguments) throws E {
        invokeFun.accept(arguments);
    }
}
