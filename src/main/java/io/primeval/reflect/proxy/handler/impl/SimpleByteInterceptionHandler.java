package io.primeval.reflect.proxy.handler.impl;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;

public final class SimpleByteInterceptionHandler implements ByteInterceptionHandler {

    @FunctionalInterface
    public interface ToByteFunction<A> {
        byte applyAsByte(A a);
    }

    private final Arguments arguments;
    private final ToByteFunction<Arguments> invokeFun;

    public SimpleByteInterceptionHandler(Arguments arguments, ToByteFunction<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> byte invoke(Arguments arguments) throws E {
        return invokeFun.applyAsByte(arguments);
    }
}
