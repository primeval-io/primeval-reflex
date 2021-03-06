package io.primeval.reflex.proxy.handler;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsProvider;

public interface VoidInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> void invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> void invoke() throws E {
        invoke(getArguments());
    }

    default public InterceptionHandler<Void> boxed() {
        return new InterceptionHandler<Void>() {

            @Override
            public <E extends Throwable> Void invoke(Arguments arguments) throws E {
                VoidInterceptionHandler.this.invoke(arguments);
                return null;
            }

            @Override
            public <E extends Throwable> Void invoke() throws E {
                VoidInterceptionHandler.this.invoke();
                return null;
            }

            @Override
            public Arguments getArguments() {
                return VoidInterceptionHandler.this.getArguments();
            }
        };
    }
}
