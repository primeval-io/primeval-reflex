package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.VoidInterceptionHandler;

public final class VoidInterceptionHelper extends InterceptionHelper {

    private final VoidInterceptionHandler handler;

    public VoidInterceptionHelper(CallContext context, VoidInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> void invoke() throws E {
        handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
