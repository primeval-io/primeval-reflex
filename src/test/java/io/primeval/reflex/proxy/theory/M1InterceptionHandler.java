package io.primeval.reflex.proxy.theory;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.handler.VoidInterceptionHandler;

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
