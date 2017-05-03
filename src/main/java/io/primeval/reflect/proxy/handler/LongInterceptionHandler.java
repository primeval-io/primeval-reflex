package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface LongInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> long invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> long invoke() throws E {
        return invoke(getArguments());
    }

    default public ObjectInterceptionHandler<Long> boxed() {
        return new ObjectInterceptionHandler<Long>() {

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
