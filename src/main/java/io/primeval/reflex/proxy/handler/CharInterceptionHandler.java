package io.primeval.reflex.proxy.handler;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsProvider;

public interface CharInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> char invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> char invoke() throws E {
        return invoke(getArguments());
    }

    default public InterceptionHandler<Character> boxed() {
        return new InterceptionHandler<Character>() {

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
