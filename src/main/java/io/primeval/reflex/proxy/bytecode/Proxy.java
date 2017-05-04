package io.primeval.reflex.proxy.bytecode;

import io.primeval.reflex.proxy.Interceptor;

public abstract class Proxy {

    protected volatile Interceptor interceptor = Interceptor.DEFAULT;

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

}
