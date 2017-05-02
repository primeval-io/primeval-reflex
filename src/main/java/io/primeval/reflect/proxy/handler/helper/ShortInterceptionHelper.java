package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;

public final class ShortInterceptionHelper extends InterceptionHelper {

    private final ShortInterceptionHandler handler;

    public ShortInterceptionHelper(CallContext context, ShortInterceptionHandler handler) {
        this.handler = handler;
    }

    public <E extends Throwable> short invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
