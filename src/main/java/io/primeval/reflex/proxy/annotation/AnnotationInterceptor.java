package io.primeval.reflex.proxy.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;

import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.Interceptor;
import io.primeval.reflex.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflex.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflex.proxy.handler.CharInterceptionHandler;
import io.primeval.reflex.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflex.proxy.handler.FloatInterceptionHandler;
import io.primeval.reflex.proxy.handler.IntInterceptionHandler;
import io.primeval.reflex.proxy.handler.InterceptionHandler;
import io.primeval.reflex.proxy.handler.LongInterceptionHandler;
import io.primeval.reflex.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflex.proxy.handler.VoidInterceptionHandler;

/**
 * <p>
 * An {@link Interceptor} that only intercepts methods that have a specific runtime annotation {@literal A}.
 * </p>
 * <p>
 * This interface make it easy to intercept annotated methods in a typesafe way.
 * </p>
 * <p>
 * If the annotation {@literal A} is {@link Repeatable}, then the Advice will be called for each instance of that annotation on the intercepted method.
 * </p>
 * 
 * @param <A>
 *            the runtime annotation to intercept.
 */
public interface AnnotationInterceptor<A extends Annotation> extends Interceptor {

    <T, E extends Throwable> T onCall(A annotation, CallContext context, InterceptionHandler<T> handler) throws E;

    default <E extends Throwable> void onCall(A annotation, CallContext context, VoidInterceptionHandler handler)
            throws E {
        onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> boolean onCall(A annotation, CallContext context, BooleanInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> byte onCall(A annotation, CallContext context, ByteInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> char onCall(A annotation, CallContext context, CharInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> double onCall(A annotation, CallContext context, DoubleInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> float onCall(A annotation, CallContext context, FloatInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> int onCall(A annotation, CallContext context, IntInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> long onCall(A annotation, CallContext context, LongInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    default <E extends Throwable> short onCall(A annotation, CallContext context, ShortInterceptionHandler handler)
            throws E {
        return onCall(annotation, context, handler.boxed());
    }

    Class<A> intercept();

    @Override
    default <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> boolean onCall(CallContext context, BooleanInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> byte onCall(CallContext context, ByteInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> char onCall(CallContext context, CharInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> double onCall(CallContext context, DoubleInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> float onCall(CallContext context, FloatInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> int onCall(CallContext context, IntInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> long onCall(CallContext context, LongInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> short onCall(CallContext context, ShortInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            return handler.invoke();
        } else if (annotations.length == 1) {
            return onCall(annotations[0], context, handler);
        } else {
            return RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

    @Override
    default <E extends Throwable> void onCall(CallContext context, VoidInterceptionHandler handler) throws E {
        Class<A> annClass = intercept();

        A[] annotations = context.method.getAnnotationsByType(annClass);
        if (annotations.length == 0) {
            handler.invoke();
        } else if (annotations.length == 1) {
            onCall(annotations[0], context, handler);
        } else {
            RepeatedAnnotationObjectInterceptor.onCall(this, annotations, context, handler);
        }
    }

}
