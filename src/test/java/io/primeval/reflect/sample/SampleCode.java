package io.primeval.reflect.sample;

import io.primeval.reflect.arguments.Arguments;
import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.annotation.AnnotationInterceptor;
import io.primeval.reflect.proxy.bytecode.Proxy;
import io.primeval.reflect.proxy.bytecode.ProxyBuilder;
import io.primeval.reflect.proxy.bytecode.ProxyClass;
import io.primeval.reflect.proxy.composite.Interceptors;
import io.primeval.reflect.proxy.handler.InterceptionHandler;

public class SampleCode {
    public static void main(String[] args) {

        firstDemo();

        ProxyClass<AnnotatedHello> proxyClass = ProxyBuilder.build(AnnotatedHello.class,
                new Class<?>[] { Hello.class });

        AnnotatedHello annotatedHello = new AnnotatedHello();

        Proxy proxy = proxyClass.newInstance(annotatedHello);

        Interceptor overrideReturnInterceptor = new AnnotationInterceptor<OverrideReturn>() {

            @SuppressWarnings("unchecked")
            @Override
            public <T, E extends Throwable> T onCall(OverrideReturn annotation, CallContext context,
                    InterceptionHandler<T> handler) throws E {
                if (context.method.getReturnType() != String.class) {
                    // only change methods returning a String
                    return handler.invoke();
                }
                return (T) annotation.value();
            }

            @Override
            public Class<OverrideReturn> intercept() {
                return OverrideReturn.class;
            }
        };
        proxy.setInterceptor(overrideReturnInterceptor);
        
        Hello annotatedHelloProxy = (Hello) proxy;
        String helloMsg = annotatedHelloProxy.getHello("world");
        
        System.out.println(helloMsg);

    }

    private static void firstDemo() {
        ProxyClass<HelloImpl> proxyClass = ProxyBuilder.build(HelloImpl.class,
                new Class<?>[] { Hello.class });
        HelloImpl helloImpl = new HelloImpl();

        Proxy proxy = proxyClass.newInstance(helloImpl);

        Hello helloProxy = (Hello) proxy;
        {
            String helloMsg = helloProxy.getHello("world");

            System.out.println(helloMsg);
        }

        Interceptor exclamationMarkInterceptor = exclamationMarkInterceptor();
        proxy.setInterceptor(exclamationMarkInterceptor);

        {
            String helloMsg = helloProxy.getHello("world");

            System.out.println(helloMsg);
        }

        Interceptor universeInterceptor = universeInterceptor();

        proxy.setInterceptor(universeInterceptor);

        {
            String helloMsg = helloProxy.getHello("world");

            System.out.println(helloMsg);
        }

        Interceptor composedInterceptor = Interceptors.compose(universeInterceptor, exclamationMarkInterceptor);
        proxy.setInterceptor(composedInterceptor);

        {
            String helloMsg = helloProxy.getHello("world");

            System.out.println(helloMsg);
        }
    }

    public static Interceptor exclamationMarkInterceptor() {
        return new Interceptor() {

            @SuppressWarnings("unchecked")
            @Override
            public <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler) throws E {
                if (context.method.getReturnType() != String.class) {
                    // only change methods returning a String
                    return handler.invoke();
                }
                String result = (String) handler.invoke();
                return (T) (result + "!"); // add exclamation mark
            }
        };
    }

    public static Interceptor universeInterceptor() {
        return new Interceptor() {

            @Override
            public <T, E extends Throwable> T onCall(CallContext context, InterceptionHandler<T> handler) throws E {
                if (context.parameters.size() < 1 || context.parameters.get(0).getType() != String.class) {
                    // only change methods taking a first parameter of String type
                    return handler.invoke();
                }
                String firstParameterName = context.parameters.get(0).getName();
                String originalArgumentValue = handler.getArguments().objectArg(firstParameterName);
                String newArgumentValue = originalArgumentValue + "^Wuniverse";
                Arguments newArguments = handler.getArguments().updater()
                        .setObjectArg(firstParameterName, newArgumentValue).update();

                return handler.invoke(newArguments);
            }
        };
    }
}
