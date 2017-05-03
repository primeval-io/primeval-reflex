package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface CharInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> char invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> char invoke() throws E {
        return invoke(getArguments());
    }

    default public ObjectInterceptionHandler<Character> boxed() {
        return new ObjectInterceptionHandler<Character>() {

            @Override
            public <E extends Throwable> Character invoke(Arguments arguments) throws E {
                return CharInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Character invoke() throws E {
                return CharInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return CharInterceptionHandler.this.getArguments();
            }
        };
    }
}
