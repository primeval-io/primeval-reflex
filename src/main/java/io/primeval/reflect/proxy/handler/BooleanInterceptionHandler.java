package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface BooleanInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> boolean invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> boolean invoke() throws E {
        return invoke(getArguments());
    }

    default public InterceptionHandler<Boolean> boxed() {
        return new InterceptionHandler<Boolean>() {

            @Override
            public <E extends Throwable> Boolean invoke(Arguments arguments) throws E {
                return BooleanInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Boolean invoke() throws E {
                return BooleanInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return BooleanInterceptionHandler.this.getArguments();
            }
        };
    }
}
