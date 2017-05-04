package io.primeval.reflex.proxy.handler.helper;

import io.primeval.reflex.arguments.ArgumentsProvider;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.handler.ByteInterceptionHandler;

public final class ByteInterceptionHelper extends InterceptionHelper {

    private final ByteInterceptionHandler handler;

    public ByteInterceptionHelper(CallContext context, ByteInterceptionHandler handler) {
        super(handler);
        this.handler = handler;
    }

    public <E extends Throwable> byte invoke() throws E {
        return handler.invoke(arguments);
    }

    @Override
    protected ArgumentsProvider argumentsProvider() {
        return handler;
    }

}
