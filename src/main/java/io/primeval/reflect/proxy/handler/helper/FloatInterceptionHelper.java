package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;

public final class FloatInterceptionHelper extends InterceptionHelper {

    private final FloatInterceptionHandler handler;

    public FloatInterceptionHelper(CallContext context, FloatInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> float invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
