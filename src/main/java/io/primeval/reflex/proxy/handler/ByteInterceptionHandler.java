package io.primeval.reflex.proxy.handler;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.arguments.ArgumentsProvider;

public interface ByteInterceptionHandler extends ArgumentsProvider {

    // Proceed with overridden arguments and return the normal value
    <E extends Throwable> byte invoke(Arguments arguments) throws E;

    // Proceed with default arguments and return the normal value
    default <E extends Throwable> byte invoke() throws E {
        return invoke(getArguments());
    }

    default public InterceptionHandler<Byte> boxed() {
        return new InterceptionHandler<Byte>() {

            @Override
            public <E extends Throwable> Byte invoke(Arguments arguments) throws E {
                return ByteInterceptionHandler.this.invoke(arguments);
            }

            @Override
            public <E extends Throwable> Byte invoke() throws E {
                return ByteInterceptionHandler.this.invoke();
            }

            @Override
            public Arguments getArguments() {
                return ByteInterceptionHandler.this.getArguments();
            }
        };
    }
}
