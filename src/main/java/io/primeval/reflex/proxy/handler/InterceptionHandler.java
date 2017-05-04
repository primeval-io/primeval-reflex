package io.primeval.reflex.proxy.handler;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsProvider;

public interface InterceptionHandler<T> extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> T invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> T invoke() throws E {
        return invoke(getArguments());
    }

}
