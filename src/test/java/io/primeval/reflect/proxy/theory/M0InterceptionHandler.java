package io.primeval.reflect.proxy.theory;

import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;

public final class M0InterceptionHandler implements ObjectInterceptionHandler<String> {

    private final TheoreticalDelegate delegate;

    public M0InterceptionHandler(TheoreticalDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public Arguments getArguments() {
        return Arguments.EMPTY_ARGUMENTS;
    }

    @Override
    public String invoke(Arguments arguments) throws Exception {
        return delegate.hello();
    }

}
