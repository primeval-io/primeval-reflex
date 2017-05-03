package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface ShortInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> short invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> short invoke() throws E {
        return invoke(getArguments());
    }

    default public InterceptionHandler<Short> boxed() {
        return new InterceptionHandler<Short>() {

            @Override
            public <E extends Throwable> Short invoke(Arguments arguments) throws E {
                return ShortInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Short invoke() throws E {
                return ShortInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return ShortInterceptionHandler.this.getArguments();
            }
        };
    }
}
