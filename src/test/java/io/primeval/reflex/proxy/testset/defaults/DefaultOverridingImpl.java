package io.primeval.reflex.proxy.testset.defaults;

public final class DefaultOverridingImpl implements DefaultItf {

    @Override
    public Class<?> myDefault() {
        return this.getClass();
    }
}
