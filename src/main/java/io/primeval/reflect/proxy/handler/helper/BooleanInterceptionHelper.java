package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;

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
