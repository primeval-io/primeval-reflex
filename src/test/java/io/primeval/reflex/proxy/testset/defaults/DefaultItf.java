package io.primeval.reflex.proxy.testset.defaults;

public interface DefaultItf {

    default Class<?> myDefault() {
        return this.getClass();
    }
}
