package io.primeval.reflect.proxy.handler.helper;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.arguments.ArgumentsProvider;
import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

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

    public static BooleanInterceptionHelper create(CallContext context, BooleanInterceptionHandler handler) {
        return new BooleanInterceptionHelper(context, handler);
    }

    public static ByteInterceptionHelper create(CallContext context, ByteInterceptionHandler handler) {
        return new ByteInterceptionHelper(context, handler);
    }

    public static CharInterceptionHelper create(CallContext context, CharInterceptionHandler handler) {
        return new CharInterceptionHelper(context, handler);
    }

    public static LongInterceptionHelper create(CallContext context, LongInterceptionHandler handler) {
        return new LongInterceptionHelper(context, handler);
    }

    public static ShortInterceptionHelper create(CallContext context, ShortInterceptionHandler handler) {
        return new ShortInterceptionHelper(context, handler);
    }

    public static VoidInterceptionHelper create(CallContext context, VoidInterceptionHandler handler) {
        return new VoidInterceptionHelper(context, handler);
    }
}
