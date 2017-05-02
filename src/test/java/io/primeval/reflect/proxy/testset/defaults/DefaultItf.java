package io.primeval.reflect.proxy.testset.defaults;

public interface DefaultItf {

    default Class<?> myDefault() {
        return this.getClass();
    }
}
