package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.FloatInterceptionHandler;

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
