package io.primeval.reflex.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import io.primeval.reflex.arguments.Arguments;
import io.primeval.reflex.proxy.CallContext;
import io.primeval.reflex.proxy.Interceptor;
import io.primeval.reflex.proxy.Interceptors;
import io.primeval.reflex.proxy.handler.InterceptionHandler;
import io.primeval.reflex.proxy.shared.SharedProxyUtils;

public class StackedInterceptorTest {

    @Test
    public void testShouldCompose() {

        Method meth = SharedProxyUtils.getMethodUnchecked(StackedInterceptorTest.class, "helloName", String.class);
        CallContext cc = new CallContext(StackedInterceptorTest.class, meth,
                Arrays.asList(meth.getParameters()));

        String firstParamName = cc.parameters.get(0).getName();

        InterceptionHandler<String> terminalInterceptionHandler = new InterceptionHandler<String>() {

            @Override
            public Arguments getArguments() {
                ArgumentsMock argumentsMock = new ArgumentsMock(cc.parameters);
                argumentsMock.setObjectArg(firstParamName, "world");
                return argumentsMock;
            }

            @Override
            public <E extends Throwable> String invoke(Arguments arguments) throws E {
                String name = arguments.objectArg(firstParamName);
                return helloName(name);
            }
        };

        Interceptor intercept1 = new Interceptor() {

            @SuppressWarnings("unchecked")
            @Override
            public <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler)
                    throws E {
                Arguments arguments = handler.getArguments().updater().setObjectArg(firstParamName, "universe")
                        .update();
                try {
                    return handler.invoke(arguments);
                } catch (IllegalStateException e) {
                    return (T) (((String) handler.invoke(arguments)) + " (retried)");
                }
            }
        };

        Interceptor intercept2 = new Interceptor() {

            int called = 0;

            @Override
            public <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler)
                    throws E {
                try {
                    if (called % 3 == 0) {
                        throw new IllegalStateException("Something wrong happened!");
                    }
                    return handler.invoke();
                } finally {
                    called++;
                }
            }
        };

        Interceptor composedInterceptor = Interceptors.stack(intercept1, intercept2);

        Assertions.assertThat(helloName("world")).isEqualTo("Hello world");

        {
            String res = terminalInterceptionHandler.invoke();
            Assertions.assertThat(res).isEqualTo("Hello world");
        }

        {
            String res = intercept1.onCall(cc, terminalInterceptionHandler);
            Assertions.assertThat(res).isEqualTo("Hello universe");
        }
        
        {
            String res = composedInterceptor.onCall(cc, terminalInterceptionHandler);
            Assertions.assertThat(res).isEqualTo("Hello universe (retried)");
        }

        {
            String res = composedInterceptor.onCall(cc, terminalInterceptionHandler);
            Assertions.assertThat(res).isEqualTo("Hello universe");
        }

    }

    public String helloName(String name) {
        return "Hello " + name;
    }

}
