package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;

public final class IntInterceptionHelper extends InterceptionHelper {

    private final IntInterceptionHandler handler;

    public IntInterceptionHelper(CallContext context, IntInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> int invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
