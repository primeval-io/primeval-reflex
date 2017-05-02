package io.primeval.reflect.proxy.testset.defaults;

public final class DefaultOverridingImpl implements DefaultItf {

    @Override
    public Class<?> myDefault() {
        return this.getClass();
    }
}
