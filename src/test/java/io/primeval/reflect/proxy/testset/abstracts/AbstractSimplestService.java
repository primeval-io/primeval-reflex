package io.primeval.reflect.proxy.testset.abstracts;

import io.primeval.reflect.proxy.testset.api.SimplestInterface;

public abstract class AbstractSimplestService implements SimplestInterface {
    public static final String PROP_NAME = "simplestAbstractedFoo";

    @Override
    public void foo() {
        System.setProperty(PROP_NAME, "true");
    }
}
