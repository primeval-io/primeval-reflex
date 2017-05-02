package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;

public final class DoubleInterceptionHelper extends InterceptionHelper {

    private final DoubleInterceptionHandler handler;

    public DoubleInterceptionHelper(CallContext context, DoubleInterceptionHandler handler) {
        this.handler = handler;
    }

    public <E extends Throwable> double invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
