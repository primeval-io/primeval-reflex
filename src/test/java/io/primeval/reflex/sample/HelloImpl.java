package io.primeval.reflex.sample;

public final class HelloImpl implements Hello {

    @Override
    public String getHello(String name) {
        return "Hello " + name;
    }

}