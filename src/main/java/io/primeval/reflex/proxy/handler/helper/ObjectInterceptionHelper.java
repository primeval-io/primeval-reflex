package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.InterceptionHandler;

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
