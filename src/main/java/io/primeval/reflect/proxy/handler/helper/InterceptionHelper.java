package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;

public abstract class InterceptionHelper {

    protected Arguments arguments = argumentsProvider().getArguments();

    protected abstract ArgumentsProvider argumentsProvider();

    public Arguments getCurrentArguments() {
        return arguments;
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public static <T> ObjectInterceptionHelper<T> create(CallContext context, ObjectInterceptionHandler<T> handler) {
        return new ObjectInterceptionHelper<>(context, handler);

    }

    public static IntInterceptionHelper create(CallContext context, IntInterceptionHandler handler) {
        return new IntInterceptionHelper(context, handler);

    }
}
