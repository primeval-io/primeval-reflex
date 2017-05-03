package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;

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
