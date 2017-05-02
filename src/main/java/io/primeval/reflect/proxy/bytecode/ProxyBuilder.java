package io.primeval.reflect.proxy.bytecode;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.primeval.reflect.proxy.bytecode.shared.Proxy;

/**
 * Entry point to proxy a class and specify which interfaces to implement
 * 
 * @author Simon Chemouil
 *
 */
public final class ProxyBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyBuilder.class);

    public static <T> ProxyClass<T> build(ProxyClassLoader dynamicClassLoader, Class<T> targetClass,
            Class<?>[] interfaces) {
        return build(dynamicClassLoader, targetClass, interfaces, m -> true);
    }

    public static <T> ProxyClass<T> build(ProxyClassLoader dynamicClassLoader, Class<T> targetClass,
            Class<?>[] interfaces, Predicate<Method> shouldIntercept) {
        try {
            Method[] methods = getMethods(targetClass);

            dynamicClassLoader.declareClassToProxy(targetClass, interfaces, methods, shouldIntercept);

            String className = ProxyClassGenerator.getName(targetClass);
            Class<?> wovenClass = dynamicClassLoader.loadClass(className);

            return new ProxyClass<T>() {

                @SuppressWarnings("unchecked")
                @Override
                public Class<T> targetClass() {
                    return (Class<T>) wovenClass;
                }

                @Override
                public Proxy newInstance(T target) {
                    return ReflectUtils.trust(() -> (Proxy) wovenClass.getConstructor(targetClass).newInstance(target));
                }
            };
        } catch (Exception e) {
            LOGGER.error("Could not weave class {}", targetClass.getName());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /* @VisibleForTesting */
    static Method[] getMethods(Class<?> clazzToWeave) {
        Method[] methods = clazzToWeave.getMethods();

        // group methods by name and parameters (unique dispatch)
        Map<MethodIdentifier, List<Method>> uniqueMethods = Stream.of(methods)
                .filter(m -> !m.isDefault() && m.getDeclaringClass() != Object.class)
                .collect(Collectors.groupingBy(m -> new MethodIdentifier(m.getName(), m.getParameterTypes())));

        return uniqueMethods.values().stream()
                // Get array of return types for each unique method
                .map(lm -> lm.toArray(new Method[0]))
                // Choose the narrowest each time
                .map(ProxyBuilder::getNarrowest).toArray(Method[]::new);

    }

    /* @VisibleForTesting */
    static Method getNarrowest(Method[] sameDispatchMethods) {
        assert sameDispatchMethods.length != 0;
        // this take care of void + all primitive types
        if (sameDispatchMethods.length == 1) {
            return sameDispatchMethods[0];
        }
        // this should take care of erased type narrowing
        Method res = null;
        Class<?> current = Object.class;
        for (Method m : sameDispatchMethods) {
            Class<?> cls = m.getReturnType();
            if (cls.isAssignableFrom(current)) {
                current = cls;
                res = m;
            }
        }
        assert res != null;
        return res;
    }
}
