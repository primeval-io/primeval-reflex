package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;

public final class LongInterceptionHelper extends InterceptionHelper {

    private final LongInterceptionHandler handler;

    public LongInterceptionHelper(CallContext context, LongInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> long invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
