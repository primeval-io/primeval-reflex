package io.primeval.reflect.proxy.theory;

import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

public final class M1InterceptionHandler implements VoidInterceptionHandler {

    private final TheoreticalDelegate delegate;
    private final M1Args arguments;

    public M1InterceptionHandler(TheoreticalDelegate delegate, M1Args arguments) {
        this.delegate = delegate;
        this.arguments = arguments;
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public void invoke(Arguments arguments) throws Exception {
        if (arguments instanceof M1Args) {
            M1Args m1Args = (M1Args) arguments;
            delegate.test(m1Args.ps, m1Args.i, m1Args.b, m1Args.s);
        } else {
            delegate.test(arguments.objectArg("ps"), arguments.intArg("i"), arguments.byteArg("b"),
                    arguments.objectArg("s"));
        }
    }

}
