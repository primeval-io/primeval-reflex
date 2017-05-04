package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.ShortInterceptionHandler;

public final class ShortInterceptionHelper extends InterceptionHelper {

    private final ShortInterceptionHandler handler;

    public ShortInterceptionHelper(CallContext context, ShortInterceptionHandler handler) {
        super(handler);
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
