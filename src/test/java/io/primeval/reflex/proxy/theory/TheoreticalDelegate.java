package io.primeval.reflex.proxy.theory;

import java.io.PrintStream;

public final class TheoreticalDelegate implements Goodbye, Hello, Stuff {

    @Override
    public void test(PrintStream ps, int i, byte b, String s) {
    }

    @Override
    public double foo(double a, int[] b) {
        return a + b[0];
    }

    @Override
    public String hello() {
        return "hello";
    }

    @Override
    public String goodbye() {
        return "goodbye";
    }

}
