package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.BooleanInterceptionHandler;

public final class BooleanInterceptionHelper extends InterceptionHelper {

    private final BooleanInterceptionHandler handler;

    public BooleanInterceptionHelper(CallContext context, BooleanInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> boolean invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
