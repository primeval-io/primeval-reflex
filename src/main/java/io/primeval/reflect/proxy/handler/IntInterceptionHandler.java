package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface IntInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> int invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> int invoke() throws E {
        return invoke(getArguments());
    }

    default public ObjectInterceptionHandler<Integer> boxed() {
        return new ObjectInterceptionHandler<Integer>() {

            @Override
            public <E extends Throwable> Integer invoke(Arguments arguments) throws E {
                return IntInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Integer invoke() throws E {
                return IntInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return IntInterceptionHandler.this.getArguments();
            }
        };
    }
}
