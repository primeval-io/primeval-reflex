package io.primeval.reflect.proxy.theory;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.bytecode.shared.Proxy;
import io.primeval.reflect.proxy.bytecode.shared.ProxyUtils;
import io.primeval.reflect.proxy.handler.DoubleInterceptionHandler;
import io.primeval.reflect.proxy.handler.VoidInterceptionHandler;

public final class TheoreticalProxy extends Proxy implements Hello, Goodbye, Stuff {

    private final static Method meth0 = ProxyUtils.getMethodUnchecked(TheoreticalDelegate.class, "hello");
    private final static CallContext cc0 = new CallContext(TheoreticalDelegate.class, meth0,
            Arrays.asList(meth0.getParameters()));

    private final static Method meth1 = ProxyUtils.getMethodUnchecked(TheoreticalDelegate.class, "test",
            PrintStream.class, int.class,
            byte.class, String.class);
    private final static CallContext cc1 = new CallContext(TheoreticalDelegate.class, meth1,
            Arrays.asList(meth1.getParameters()));

    private final static Method meth2 = ProxyUtils.getMethodUnchecked(TheoreticalDelegate.class, "foo", double.class,
            int[].class,
            byte.class, String.class);
    private final static CallContext cc2 = new CallContext(TheoreticalDelegate.class, meth2,
            Arrays.asList(meth2.getParameters()));

    private final TheoreticalDelegate delegate;
    private final M0ObjectInterceptionHandler handler0;

    public TheoreticalProxy(TheoreticalDelegate delegate) {
        this.delegate = delegate;
        this.handler0 = new M0ObjectInterceptionHandler(delegate);
    }

    @Override
    public String hello() {
        return interceptor.onCall(cc0, handler0);
    }

    @Override
    public void test(PrintStream ps, int i, byte b, String s) {
        interceptor.onCall(cc1, new VoidInterceptionHandler() {
            Arguments args = null;

            @Override
            public Arguments getArguments() {
                if (args == null) {
                    args = new M1Args(cc1.parameters, ps, i, b, s);
                }
                return args;
            }

            @Override
            public void invoke() {
                delegate.test(ps, i, b, s);
            }

            @Override
            public void invoke(Arguments arguments) {
                delegate.test(ps, i, b, s); // fixed
            }
        });

    }

    @Override
    public double foo(double a, int[] b) {
        return interceptor.onCall(cc2, new DoubleInterceptionHandler() {

            @Override
            public Arguments getArguments() {
                return Arguments.EMPTY_ARGUMENTS;
            }

            @Override
            public double invoke() throws Exception {
                return delegate.foo(a, b);
            }

            @Override
            public double invoke(Arguments arguments) throws Exception {
                return delegate.foo(a, b);
            }

        });

    }

    @Override
    // Not intercepted
    public String goodbye() {
        return delegate.goodbye();
    }

    public AssertionError newChecked() {
        throw new AssertionError();
    }

}
