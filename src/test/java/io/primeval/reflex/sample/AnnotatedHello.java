package io.primeval.reflex.sample;

public final class AnnotatedHello implements Hello {

    @Override
    @OverrideReturn("Goodbye!")
    public String getHello(String name) {
        return "Hello " + name;
    }

}