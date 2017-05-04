package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.DoubleInterceptionHandler;

public final class DoubleInterceptionHelper extends InterceptionHelper {

    private final DoubleInterceptionHandler handler;

    public DoubleInterceptionHelper(CallContext context, DoubleInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> double invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
