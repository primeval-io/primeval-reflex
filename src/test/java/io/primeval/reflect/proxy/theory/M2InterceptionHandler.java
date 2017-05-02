package io.primeval.reflect.proxy.theory;

import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;

public final class M2InterceptionHandler implements DoubleInterceptionHandler {

    private final TheoreticalDelegate delegate;
    private final Arguments arguments;

    public M2InterceptionHandler(TheoreticalDelegate delegate, Arguments arguments) {
        this.delegate = delegate;
        this.arguments = arguments;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public double invoke(Arguments arguments) throws Exception {
        return delegate.foo(42.0, null);
    }

}
