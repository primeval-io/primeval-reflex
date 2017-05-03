package io.primeval.reflect.proxy.bytecode;

import io.primeval.reflect.proxy.shared.Proxy;

public interface ProxyClass<T> {

    Class<T> targetClass();

    Proxy newInstance(T target);

}
