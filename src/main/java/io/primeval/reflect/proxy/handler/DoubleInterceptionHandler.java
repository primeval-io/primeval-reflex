package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface DoubleInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> double invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> double invoke() throws E {
        return invoke(getArguments());
    }

    default public InterceptionHandler<Double> boxed() {
        return new InterceptionHandler<Double>() {

            @Override
            public <E extends Throwable> Double invoke(Arguments arguments) throws E {
                return DoubleInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Double invoke() throws E {
                return DoubleInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return DoubleInterceptionHandler.this.getArguments();
            }
        };
    }
}
