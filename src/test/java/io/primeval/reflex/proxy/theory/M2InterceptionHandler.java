package io.primeval.reflex.proxy.theory;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.DoubleInterceptionHandler;

public final class M2InterceptionHandler implements DoubleInterceptionHandler {

    private final TheoreticalDelegate delegate;
    private final M2Args arguments;

    public M2InterceptionHandler(TheoreticalDelegate delegate, M2Args arguments) {
        this.delegate = delegate;
        this.arguments = arguments;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public double invoke(Arguments arguments) throws Exception {
        if (arguments instanceof M2Args) {
            M2Args m2Args = (M2Args) arguments;
            return delegate.foo(m2Args.a, m2Args.b);
        }
        return delegate.foo(arguments.doubleArg("a"), arguments.objectArg("b"));
    }
    
    @Override
    public <E extends Throwable> double invoke() throws E {
        M2Args m2Args = arguments;
        return delegate.foo(m2Args.a, m2Args.b);
    }

}
