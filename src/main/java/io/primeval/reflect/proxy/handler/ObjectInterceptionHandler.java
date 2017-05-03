package io.primeval.reflect.proxy.handler;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.arguments.ArgumentsProvider;

public interface ObjectInterceptionHandler<T> extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> T invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> T invoke() throws E {
        return invoke(getArguments());
    }

}
