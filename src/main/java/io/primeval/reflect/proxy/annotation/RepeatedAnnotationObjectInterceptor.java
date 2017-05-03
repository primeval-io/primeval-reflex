package io.primeval.reflect.proxy.annotation;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
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

public final class RepeatedAnnotationObjectInterceptor {

    public static <A extends Annotation, T, E extends Throwable> T onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            InterceptionHandler<T> handler) throws E {
        return RepeatedAnnotationObjectInterceptor.<A, T, RuntimeException> callObject(annotationInterceptor, 0,
                annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, T, E extends Throwable> T callObject(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            Function<Arguments, T> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleInterceptionHandler<>(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleInterceptionHandler<>(currentArguments,
                            (args) -> callObject(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> void onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            VoidInterceptionHandler handler) throws E {
        callVoid(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> void callVoid(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            Consumer<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            annotationInterceptor.onCall(annotation, context,
                    new SimpleVoidInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            annotationInterceptor.onCall(annotation, context,
                    new SimpleVoidInterceptionHandler(currentArguments,
                            (args) -> callVoid(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> boolean onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            BooleanInterceptionHandler handler) throws E {
        return callBoolean(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> boolean callBoolean(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            Predicate<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleBooleanInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleBooleanInterceptionHandler(currentArguments,
                            (args) -> callBoolean(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> byte onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            ByteInterceptionHandler handler) throws E {
        return callByte(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> byte callByte(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToByteFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleByteInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleByteInterceptionHandler(currentArguments,
                            (args) -> callByte(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> char onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            CharInterceptionHandler handler) throws E {
        return callChar(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> char callChar(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToCharFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleCharInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleCharInterceptionHandler(currentArguments,
                            (args) -> callChar(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> double onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            DoubleInterceptionHandler handler) throws E {
        return callDouble(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> double callDouble(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToDoubleFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleDoubleInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleDoubleInterceptionHandler(currentArguments,
                            (args) -> callDouble(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> float onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            FloatInterceptionHandler handler) throws E {
        return callFloat(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> float callFloat(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToFloatFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleFloatInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleFloatInterceptionHandler(currentArguments,
                            (args) -> callFloat(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> int onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            IntInterceptionHandler handler) throws E {
        return callInt(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> int callInt(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToIntFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleIntInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleIntInterceptionHandler(currentArguments,
                            (args) -> callInt(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> long onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            LongInterceptionHandler handler) throws E {
        return callLong(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> long callLong(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToLongFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleLongInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleLongInterceptionHandler(currentArguments,
                            (args) -> callLong(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }

    public static <A extends Annotation, E extends Throwable> short onCall(
            AnnotationInterceptor<A> annotationInterceptor, A[] annotations, CallContext context,
            ShortInterceptionHandler handler) throws E {
        return callShort(annotationInterceptor, 0, annotations, context, handler.getArguments(), handler::invoke);
    }

    private static <A extends Annotation, E extends Throwable> short callShort(
            AnnotationInterceptor<A> annotationInterceptor,
            int annotationId, A[] annotations, CallContext context, Arguments currentArguments,
            ToShortFunction<Arguments> terminalInvokeFun) throws E {

        A annotation = annotations[annotationId];
        if (annotationId == annotations.length - 1) { // last annotation
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleShortInterceptionHandler(currentArguments, terminalInvokeFun));
        } else {
            return annotationInterceptor.onCall(annotation, context,
                    new SimpleShortInterceptionHandler(currentArguments,
                            (args) -> callShort(annotationInterceptor, annotationId + 1, annotations, context, args,
                                    terminalInvokeFun)));
        }
    }
}
