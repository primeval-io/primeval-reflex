package io.primeval.reflex.proxy.handler;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsProvider;

public interface LongInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> long invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> long invoke() throws E {
        return invoke(getArguments());
    }

    default public InterceptionHandler<Long> boxed() {
        return new InterceptionHandler<Long>() {

            @Override
            public <E extends Throwable> Long invoke(Arguments arguments) throws E {
                return LongInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Long invoke() throws E {
                return LongInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return LongInterceptionHandler.this.getArguments();
            }
        };
    }
}
