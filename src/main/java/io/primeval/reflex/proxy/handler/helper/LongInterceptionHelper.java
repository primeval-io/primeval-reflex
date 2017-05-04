package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.LongInterceptionHandler;

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
