package io.primeval.reflect.sample;

public final class HelloImpl implements Hello {

    @Override
    public String getHello(String name) {
        return "Hello " + name;
    }

}