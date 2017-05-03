package io.primeval.reflect.proxy.composite;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.InterceptionHandler;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleBooleanInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleByteInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleByteInterceptionHandler.ToByteFunction;
import io.primeval.reflect.proxy.handler.impl.SimpleCharInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleCharInterceptionHandler.ToCharFunction;
import io.primeval.reflect.proxy.handler.impl.SimpleDoubleInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleFloatInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleFloatInterceptionHandler.ToFloatFunction;
import io.primeval.reflect.proxy.handler.impl.SimpleIntInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleLongInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleShortInterceptionHandler;
import io.primeval.reflect.proxy.handler.impl.SimpleShortInterceptionHandler.ToShortFunction;
import io.primeval.reflect.proxy.handler.impl.SimpleVoidInterceptionHandler;

/**
 * An Interceptor composed of several Interceptors. Build with {@link Interceptors#compose(Interceptor...)}.
 */
final class CompositeInterceptor implements Interceptor {

    private final Interceptor[] interceptors;
    private String repr;

    public CompositeInterceptor(Interceptor[] interceptors) {
        if (interceptors == null || interceptors.length == 0) {
            throw new IllegalArgumentException("interceptors must be non-empty");
        }
        this.interceptors = interceptors;
    }

    @Override
    public String toString() {
        if (repr == null) {
            repr = new StringBuilder().append("Composite{").append(String.join(",", new Iterable<String>() {
                public Iterator<String> iterator() {
                    return Stream.of(interceptors).map(Object::toString).iterator();
                }
            })).append('}').toString();
        }
        return repr;

    }

    @Override
    public <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler) throws E {
        return CompositeInterceptor.<T, RuntimeException> callObject(0, interceptors, context, handler.getArguments(),
                handler::invoke);
    }

    private static <T, E extends Throwable> T callObject(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            Function<Arguments, T> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context,
                    new SimpleInterceptionHandler<>(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleInterceptionHandler<>(currentArguments,
                    (args) -> callObject(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> void onCall(CallContext context, VoidInterceptionHandler handler) throws E {
        callVoid(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    private static <E extends Throwable> void callVoid(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            Consumer<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            interceptor.onCall(context, new SimpleVoidInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            interceptor.onCall(context, new SimpleVoidInterceptionHandler(currentArguments,
                    (args) -> callVoid(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> int onCall(CallContext context, IntInterceptionHandler handler) throws E {
        return callInt(0, interceptors, context, handler.getArguments(),
                handler::invoke);
    }

    private static <E extends Throwable> int callInt(int interceptorId, Interceptor[] interceptors, CallContext context,
            Arguments currentArguments,
            ToIntFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, new SimpleIntInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleIntInterceptionHandler(currentArguments,
                    (args) -> callInt(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> byte onCall(CallContext context, ByteInterceptionHandler handler) throws E {
        return callByte(0, interceptors, context, handler.getArguments(),
                handler::invoke);
    }

    private static <E extends Throwable> byte callByte(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            ToByteFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context,
                    new SimpleByteInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context,
                    new SimpleByteInterceptionHandler(currentArguments,
                            (args) -> callByte(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> boolean onCall(CallContext context, BooleanInterceptionHandler handler) throws E {
        return callBoolean(0, interceptors, context, handler.getArguments(),
                handler::invoke);
    }

    private static <E extends Throwable> boolean callBoolean(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            Predicate<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context,
                    new SimpleBooleanInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleBooleanInterceptionHandler(currentArguments,
                    (args) -> callBoolean(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> short onCall(CallContext context, ShortInterceptionHandler handler) throws E {
        return callShort(0, interceptors, context, handler.getArguments(),
                handler::invoke);
    }

    private static <E extends Throwable> short callShort(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments,
            ToShortFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, new SimpleShortInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleShortInterceptionHandler(currentArguments,
                    (args) -> callShort(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> char onCall(CallContext context, CharInterceptionHandler handler) throws E {
        return callChar(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    private static <E extends Throwable> char callChar(int interceptorId, Interceptor[] interceptors,
            CallContext context,
            Arguments currentArguments, ToCharFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, new SimpleCharInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleCharInterceptionHandler(currentArguments,
                    (args) -> callChar(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> double onCall(CallContext context, DoubleInterceptionHandler handler) throws E {
        return callDouble(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    private static <E extends Throwable> double callDouble(int interceptorId, Interceptor[] interceptors,
            CallContext context, Arguments currentArguments, ToDoubleFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context,
                    new SimpleDoubleInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleDoubleInterceptionHandler(currentArguments,
                    (args) -> callDouble(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> float onCall(CallContext context, FloatInterceptionHandler handler) throws E {
        return callFloat(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    private static <E extends Throwable> float callFloat(int interceptorId, Interceptor[] interceptors,
            CallContext context, Arguments currentArguments, ToFloatFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, new SimpleFloatInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleFloatInterceptionHandler(currentArguments,
                    (args) -> callFloat(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

    @Override
    public <E extends Throwable> long onCall(CallContext context, LongInterceptionHandler handler) throws E {
        return callLong(0, interceptors, context, handler.getArguments(), handler::invoke);
    }

    private static <E extends Throwable> long callLong(int interceptorId, Interceptor[] interceptors,
            CallContext context, Arguments currentArguments, ToLongFunction<Arguments> terminalInvokeFun) throws E {
        Interceptor interceptor = interceptors[interceptorId];
        if (interceptorId == interceptors.length - 1) { // last interceptor
            return interceptor.onCall(context, new SimpleLongInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return interceptor.onCall(context, new SimpleLongInterceptionHandler(currentArguments,
                    (args) -> callLong(interceptorId + 1, interceptors, context, args, terminalInvokeFun)));
        }
    }

}
