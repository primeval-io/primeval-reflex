package io.primeval.reflect.proxy.bytecode;

import io.primeval.reflect.proxy.Interceptor;

public abstract class Proxy {

    protected volatile Interceptor interceptor = Interceptor.DEFAULT;

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

}
