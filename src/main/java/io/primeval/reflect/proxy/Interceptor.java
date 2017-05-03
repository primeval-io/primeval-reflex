package io.primeval.reflect.proxy;

import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;
import io.primeval.reflect.proxy.handler.InterceptionHandler;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

public interface Interceptor {

    <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler) throws E;

    default <E extends Throwable> void onCall(CallContext context, VoidInterceptionHandler handler) throws E {
        onCall(context, handler.boxed());
    }

    default <E extends Throwable> boolean onCall(CallContext context, BooleanInterceptionHandler handler)
            throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> byte onCall(CallContext context, ByteInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> char onCall(CallContext context, CharInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> short onCall(CallContext context, ShortInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> int onCall(CallContext context, IntInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> long onCall(CallContext context, LongInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> float onCall(CallContext context, FloatInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    default <E extends Throwable> double onCall(CallContext context, DoubleInterceptionHandler handler) throws E {
        return onCall(context, handler.boxed());
    }

    public static final Interceptor DEFAULT = new Interceptor() {

        @Override
        public <E extends Throwable> void onCall(CallContext context, VoidInterceptionHandler handler) throws E {
            handler.invoke();
        }

        @Override
        public <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> boolean onCall(CallContext context, BooleanInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> byte onCall(CallContext context, ByteInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> char onCall(CallContext context, CharInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> short onCall(CallContext context, ShortInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> int onCall(CallContext context, IntInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> long onCall(CallContext context, LongInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> float onCall(CallContext context, FloatInterceptionHandler handler) throws E {
            return handler.invoke();
        }

        @Override
        public <E extends Throwable> double onCall(CallContext context, DoubleInterceptionHandler handler) throws E {
            return handler.invoke();
        }
    };
}
