package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.CharInterceptionHandler;

public final class CharInterceptionHelper extends InterceptionHelper {

    private final CharInterceptionHandler handler;

    public CharInterceptionHelper(CallContext context, CharInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> char invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
