package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.handler.InterceptionHandler;

public final class ObjectInterceptionHelper<T> extends InterceptionHelper {

    private final InterceptionHandler<T> handler;

    public ObjectInterceptionHelper(CallContext context, InterceptionHandler<T> handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> T invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
