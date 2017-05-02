package io.primeval.reflect.proxy.testset.simpleservice;

import java.io.PrintStream;

import io.primeval.reflect.proxy.testset.api.BadValueException;
import io.primeval.reflect.proxy.testset.api.SimpleInterface;

public final class DelegatingSimpleService implements SimpleInterface {

    private final SimpleService delegate;

    public DelegatingSimpleService(SimpleService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sayHello(PrintStream ps) {
        delegate.sayHello(ps);
    }

    @Override
    public String hello() {
        return delegate.hello();
    }

    @Override
    public int times() {
        return delegate.times();
    }

    @Override
    public int increase(int a) throws BadValueException {
        return delegate.increase(a);
    }

    @Override
    public int reduce(int[] arr) {
        return delegate.reduce(arr);
    };

}
