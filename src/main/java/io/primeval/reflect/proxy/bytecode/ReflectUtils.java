package io.primeval.reflect.proxy.bytecode;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.primeval.reflect.proxy.handler.BooleanInterceptionHandler;
import io.primeval.reflect.proxy.handler.ByteInterceptionHandler;
import io.primeval.reflect.proxy.handler.CharInterceptionHandler;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflect.proxy.handler.FloatInterceptionHandler;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.LongInterceptionHandler;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;
import io.primeval.reflect.proxy.handler.ShortInterceptionHandler;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

public final class ReflectUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtils.class);

    private ReflectUtils() {
    }

    // Run any block of code and propagate throwables as necessary
    public static <T> T trust(Callable<T> block) {
        try {
            return block.call();
        } catch (RuntimeException | Error e) {
            LOGGER.error("Error while running code", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception while running code", e);
            throw new RuntimeException(e);
        }
    }

    public static String asStringProperty(Object propObj) {
        String res;
        if (propObj == null) {
            res = null;
        } else if (propObj instanceof String[] && ((String[]) propObj).length == 1) {
            res = ((String[]) propObj)[0];
        } else if (propObj instanceof String) {
            res = (String) propObj;
        } else {
            throw new IllegalArgumentException("Can only convert properties of type String or String[] of size 1");
        }
        return res;
    }

    public static String[] asStringProperties(Object propObj) {
        String[] res = null;
        if (propObj == null) {
            res = new String[0];
        } else if (propObj instanceof String[]) {
            res = (String[]) propObj;
        } else if (propObj instanceof String) {
            res = new String[] { (String) propObj };
        } else {
            throw new IllegalArgumentException("Can only convert properties of type String or String[]");
        }
        return res;
    }

    public static long getLongValue(Object propObj) {
        if (propObj instanceof Number) {
            return ((Number) propObj).longValue();
        } else {
            throw new IllegalArgumentException("Required number!");
        }
    }

    public static int getIntValue(Object propObj, int defaultValue) {
        if (propObj instanceof Integer) {
            return ((Integer) propObj).intValue();
        } else {
            return defaultValue;
        }
    }

    public static <T> T firstOrNull(SortedSet<T> set) {
        if (set == null || set.isEmpty()) {
            return null;
        }
        return set.first();
    }

    public static <T> Set<T> copySet(Collection<T> source) {
        Set<T> copy = new LinkedHashSet<>(source.size());
        copy.addAll(source);
        return copy;
    }
    
    static Class<?> getInterceptionHandlerClass(Class<?> returnType) {
        if (returnType == void.class) {
            return VoidInterceptionHandler.class;
        } else if (returnType == int.class) {
            return IntInterceptionHandler.class;
        } else if (returnType == short.class) {
            return ShortInterceptionHandler.class;
        } else if (returnType == double.class) {
            return DoubleInterceptionHandler.class;
        } else if (returnType == float.class) {
            return FloatInterceptionHandler.class;
        } else if (returnType == char.class) {
            return CharInterceptionHandler.class;
        } else if (returnType == long.class) {
            return LongInterceptionHandler.class;
        } else if (returnType == byte.class) {
            return ByteInterceptionHandler.class;
        } else if (returnType == boolean.class) {
            return BooleanInterceptionHandler.class;
        } else {
            return ObjectInterceptionHandler.class;
        }
    }
    
    static String makeSuffixClassDescriptor(String classToProxyDescriptor, String suffix) {
        StringBuilder buf = new StringBuilder();
        buf.append(classToProxyDescriptor, 0, classToProxyDescriptor.length() - 1); // omit
                                                                                // ';'
        buf.append(suffix);
        buf.append(';');
        return buf.toString();
    }

    static String nullToEmpty(String argsClassDescriptor) {
        if (argsClassDescriptor == null)
            return "";
        return argsClassDescriptor;
    }
}
