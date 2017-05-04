package io.primeval.reflex.proxy.testset.simplest;

import io.primeval.reflex.proxy.testset.api.SimplestInterface;

public final class SimplestService implements SimplestInterface {

    public static final String PROP_NAME = "simplestFoo";

    @Override
    public void foo() {
        System.setProperty(PROP_NAME, "true");
    }

}
