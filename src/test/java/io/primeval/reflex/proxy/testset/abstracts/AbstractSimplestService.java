package io.primeval.reflex.proxy.testset.abstracts;

import io.primeval.reflex.proxy.testset.api.SimplestInterface;

public abstract class AbstractSimplestService implements SimplestInterface {
    public static final String PROP_NAME = "simplestAbstractedFoo";

    @Override
    public void foo() {
        System.setProperty(PROP_NAME, "true");
    }
}
