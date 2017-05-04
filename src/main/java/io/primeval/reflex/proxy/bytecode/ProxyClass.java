package io.primeval.reflex.proxy.bytecode;

public interface ProxyClass<T> {

    Class<T> targetClass();

    Proxy newInstance(Object target);

}
