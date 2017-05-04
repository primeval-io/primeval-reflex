package io.primeval.reflex.proxy.handler.impl;

import java.util.function.Predicate;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.BooleanInterceptionHandler;

public final class SimpleBooleanInterceptionHandler implements BooleanInterceptionHandler {

    private final Arguments arguments;
    private final Predicate<Arguments> invokeFun;

    public SimpleBooleanInterceptionHandler(Arguments arguments, Predicate<Arguments> invokeFun) {
        this.arguments = arguments;
        this.invokeFun = invokeFun;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public <E extends Throwable> boolean invoke(Arguments arguments) throws E {
        return invokeFun.test(arguments);
    }
}
