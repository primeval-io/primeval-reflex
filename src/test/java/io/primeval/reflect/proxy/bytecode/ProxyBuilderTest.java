package io.primeval.reflect.proxy.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;
import org.junit.BeforeClass;
import org.junit.Test;

import io.primeval.reflect.proxy.CallContext;
import io.primeval.reflect.proxy.Interceptor;
import io.primeval.reflect.proxy.arguments.Arguments;
import io.primeval.reflect.proxy.bytecode.ProxyClassLoader;
import io.primeval.reflect.proxy.bytecode.shared.Proxy;
import io.primeval.reflect.proxy.bytecode.MethodIdentifier;
import io.primeval.reflect.proxy.bytecode.ProxyBuilder;
import io.primeval.reflect.proxy.bytecode.ProxyClass;
import io.primeval.reflect.proxy.handler.IntInterceptionHandler;
import io.primeval.reflect.proxy.handler.ObjectInterceptionHandler;
import io.primeval.reflect.proxy.handler.helper.IntInterceptionHelper;
import io.primeval.reflect.proxy.handler.helper.InterceptionHelper;
import io.primeval.reflect.proxy.handler.helper.ObjectInterceptionHelper;
import io.primeval.reflect.proxy.testset.abstracts.AbstractSimplestService;
import io.primeval.reflect.proxy.testset.abstracts.AbstractedOverridingSimplestService;
import io.primeval.reflect.proxy.testset.abstracts.AbstractedSimplestService;
import io.primeval.reflect.proxy.testset.annotated.AnnotatedService;
import io.primeval.reflect.proxy.testset.api.BadValueException;
import io.primeval.reflect.proxy.testset.api.GenericInterface;
import io.primeval.reflect.proxy.testset.api.SimpleInterface;
import io.primeval.reflect.proxy.testset.api.SimplestInterface;
import io.primeval.reflect.proxy.testset.api.generic.GenericParamsService;
import io.primeval.reflect.proxy.testset.api.generic.GenericService;
import io.primeval.reflect.proxy.testset.bounds.BoundsImpl;
import io.primeval.reflect.proxy.testset.bounds.BoundsItf;
import io.primeval.reflect.proxy.testset.defaults.DefaultImpl;
import io.primeval.reflect.proxy.testset.defaults.DefaultItf;
import io.primeval.reflect.proxy.testset.defaults.DefaultOverridingImpl;
import io.primeval.reflect.proxy.testset.simpleservice.SimpleService;
import io.primeval.reflect.proxy.testset.simplest.SimplestService;

public final class ProxyBuilderTest {

    private static ProxyClassLoader proxyClassLoader;

    @BeforeClass
    public static void setUp() {
        proxyClassLoader = new ProxyClassLoader(ProxyBuilderTest.class.getClassLoader());
    }

    @Test
    public void shouldWeaveSimplestClass() {
        SimplestService simplestService = new SimplestService();

        ProxyClass<SimplestService> proxyFactory = ProxyBuilder.build(proxyClassLoader, SimplestService.class,
                new Class[] { SimplestInterface.class });
        assertThat(SimplestInterface.class).isAssignableFrom(proxyFactory.targetClass());

        Object proxyService = proxyFactory.newInstance(simplestService);
        assertThat(proxyService).isInstanceOf(SimplestInterface.class);

        SimplestInterface wovenItf = (SimplestInterface) proxyService;
        try {
            System.clearProperty(SimplestService.PROP_NAME);
            wovenItf.foo();
            assertThat(System.getProperty(SimplestService.PROP_NAME)).isEqualTo("true");
        } finally {
            System.clearProperty(SimplestService.PROP_NAME);
        }
    }

    @Test
    public void shouldWeaveAbstractedSimplestClass() {
        AbstractedSimplestService simplestService = new AbstractedSimplestService();

        ProxyClass<AbstractedSimplestService> proxyFactory = ProxyBuilder.build(proxyClassLoader,
                AbstractedSimplestService.class,
                new Class[] { SimplestInterface.class });
        assertThat(SimplestInterface.class).isAssignableFrom(proxyFactory.targetClass());

        Object proxyService = proxyFactory.newInstance(simplestService);
        assertThat(proxyService).isInstanceOf(SimplestInterface.class);

        SimplestInterface wovenItf = (SimplestInterface) proxyService;
        try {
            System.clearProperty(AbstractSimplestService.PROP_NAME);
            wovenItf.foo();
            assertThat(System.getProperty(AbstractSimplestService.PROP_NAME)).isEqualTo("true");
        } finally {
            System.clearProperty(AbstractSimplestService.PROP_NAME);
        }
    }

    @Test
    public void shouldWeaveAbstractedOverridingSimplestClass() {
        AbstractedOverridingSimplestService simplestService = new AbstractedOverridingSimplestService();

        ProxyClass<AbstractedOverridingSimplestService> proxyFactory = ProxyBuilder.build(proxyClassLoader,
                AbstractedOverridingSimplestService.class,
                new Class[] { SimplestInterface.class });
        assertThat(SimplestInterface.class).isAssignableFrom(proxyFactory.targetClass());

        Object proxyService = proxyFactory.newInstance(simplestService);
        assertThat(proxyService).isInstanceOf(SimplestInterface.class);

        SimplestInterface wovenItf = (SimplestInterface) proxyService;
        try {
            System.clearProperty(AbstractedOverridingSimplestService.PROP_NAME);
            wovenItf.foo();
            assertThat(System.getProperty(AbstractedOverridingSimplestService.PROP_NAME)).isEqualTo("true");
        } finally {
            System.clearProperty(AbstractedOverridingSimplestService.PROP_NAME);
        }
    }

    @Test
    public void shouldWeaveDefaultClass() {
        DefaultImpl simplestService = new DefaultImpl();

        ProxyClass<DefaultImpl> proxyFactory = ProxyBuilder.build(proxyClassLoader, DefaultImpl.class,
                new Class[] { DefaultItf.class });
        assertThat(DefaultItf.class).isAssignableFrom(proxyFactory.targetClass());

        Object proxyService = proxyFactory.newInstance(simplestService);
        assertThat(proxyService).isInstanceOf(DefaultItf.class);

        DefaultItf wovenItf = (DefaultItf) proxyService;
        Class<?> myDefault = wovenItf.myDefault();
        assertThat(myDefault).isSameAs(proxyService.getClass());

    }

    @Test
    public void shouldWeaveOverridedDefaultClass() {
        DefaultOverridingImpl simplestService = new DefaultOverridingImpl();

        ProxyClass<DefaultOverridingImpl> proxyFactory = ProxyBuilder.build(proxyClassLoader,
                DefaultOverridingImpl.class,
                new Class[] { DefaultItf.class });
        assertThat(DefaultItf.class).isAssignableFrom(proxyFactory.targetClass());

        Proxy proxyService = proxyFactory.newInstance(simplestService);
        assertThat(proxyService).isInstanceOf(DefaultItf.class);

        DefaultItf wovenItf = (DefaultItf) proxyService;
        Class<?> myDefault = wovenItf.myDefault();
        assertThat(myDefault).isSameAs(DefaultOverridingImpl.class);
    }

    @Test
    public void shouldWeaveSimpleClass() throws IOException, BadValueException {
        SimpleService simpleService = new SimpleService();

        ProxyClass<SimpleService> proxyFactory = ProxyBuilder.build(proxyClassLoader, SimpleService.class,
                new Class[] { SimpleInterface.class });
        assertThat(SimpleInterface.class).isAssignableFrom(proxyFactory.targetClass());

        Proxy proxyService = proxyFactory.newInstance(simpleService);

        proxyService.setInterceptor(new Interceptor() {

            void updateArguments(CallContext callContext, InterceptionHelper helper) {
                Arguments arguments = helper.getCurrentArguments();
                String firstArg = callContext.parameters.get(0).getName();

                if (arguments.intArg(firstArg) < 0) {
                    helper.setArguments(arguments.updater().setIntArg(firstArg, Math.abs(arguments.intArg(firstArg)))
                            .update());
                }

            }

            @Override
            public <T, E extends Throwable> T onCall(CallContext callContext, ObjectInterceptionHandler<T> handler)
                    throws E {
                if (callContext.method.getName().equals("increase")) {
                    ObjectInterceptionHelper<T> helper = InterceptionHelper.create(callContext, handler);
                    updateArguments(callContext, helper);
                    return helper.invoke();
                }
                return handler.invoke();
            }

            @Override
            public int onCall(CallContext callContext, IntInterceptionHandler handler) throws Exception {

                if (callContext.method.getName().equals("increase")) {
                    IntInterceptionHelper helper = InterceptionHelper.create(callContext, handler);
                    updateArguments(callContext, helper);

                    int result = helper.invoke();
                    return result * 3;

                }
                return handler.invoke();

            }
        });

        assertThat(proxyService).isInstanceOf(SimpleInterface.class);

        SimpleInterface wovenItf = (SimpleInterface) proxyService;

        assertThat(simpleService.increase(10)).isEqualTo(20);
        assertThat(wovenItf.increase(10)).isEqualTo(60);

        assertThat(wovenItf.hello()).isEqualTo(simpleService.hello());

        assertThat(wovenItf.times()).isEqualTo(simpleService.times());

        assertThat(extractFromPrintStream(ps -> wovenItf.sayHello(ps)))
                .isEqualTo(extractFromPrintStream(ps -> simpleService.sayHello(ps)));
    }

    private String extractFromPrintStream(Consumer<PrintStream> psConsumer)
            throws UnsupportedEncodingException, IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos)) {
            psConsumer.accept(ps);
            return baos.toString("UTF-8");
        }
    }

    @Test
    public void shouldWeaveAnnotations()
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        ProxyClass<AnnotatedService> proxyFactory = ProxyBuilder.build(proxyClassLoader, AnnotatedService.class,
                new Class[0]);

        assertThat(proxyFactory.targetClass().getAnnotations())
                .containsExactly(AnnotatedService.class.getAnnotations());

        AnnotatedService annotatedService = new AnnotatedService();
        Object proxyService = proxyFactory.newInstance(annotatedService);

        Method method = AnnotatedService.class.getMethod("someMethod");
        Method proxyMethod = proxyFactory.targetClass().getMethod("someMethod");

        assertThat(proxyMethod.invoke(proxyService)).isEqualTo(method.invoke(annotatedService));
        assertThat(proxyMethod.getAnnotations()).containsExactly(method.getAnnotations());
    }

    @Test
    public void shouldWeaveTwice() {
        ProxyBuilder.build(proxyClassLoader, SimpleService.class, new Class[] { SimpleInterface.class });

        ProxyBuilder.build(proxyClassLoader, SimpleService.class, new Class[] { SimpleInterface.class });

    }

    @Test
    public void shouldWeaveGenericServices() {
        ProxyClass<GenericService> proxyFactory = ProxyBuilder.build(proxyClassLoader, GenericService.class,
                new Class<?>[] { GenericInterface.class });

        assertThat(GenericInterface.class).isAssignableFrom(proxyFactory.targetClass());

        assertThat(proxyFactory.targetClass().getGenericInterfaces())
                .containsExactly(GenericService.class.getGenericInterfaces());

        GenericService genericService = new GenericService();
        List<Object> mutableObjects = genericService.getObjects();

        @SuppressWarnings("unchecked")
        GenericInterface<Object, String> proxyService = (GenericInterface<Object, String>) proxyFactory
                .newInstance(genericService);

        String someB = proxyService.makeB();
        assertThat(someB).isEqualTo(genericService.makeB());
        proxyService.doSome();
        assertThat(mutableObjects).containsExactly(someB);
    }

    @Test
    public void shouldWeaveBoundedClass() throws Exception {
        ProxyClass<BoundsImpl> proxyFactory = ProxyBuilder.build(proxyClassLoader, BoundsImpl.class,
                new Class<?>[] { BoundsItf.class });

        assertThat(BoundsItf.class).isAssignableFrom(proxyFactory.targetClass());

        assertThat(proxyFactory.targetClass().getGenericInterfaces())
                .containsExactly(BoundsImpl.class.getGenericInterfaces());

        ArrayList<MethodIdentifier> methodsToCompare = Lists.newArrayList(new MethodIdentifier("someList1"),
                new MethodIdentifier("someList2"), new MethodIdentifier("someList3"), new MethodIdentifier("someList4"),
                new MethodIdentifier("singleton", Number.class), new MethodIdentifier("foo", Number.class),
                new MethodIdentifier("makeFoo", Supplier.class));
        for (MethodIdentifier methodId : methodsToCompare) {
            Method method = BoundsImpl.class.getMethod(methodId.name, methodId.parameterTypes);
            Method proxyMethod = proxyFactory.targetClass().getMethod(methodId.name, methodId.parameterTypes);

            assertThat(proxyMethod.getAnnotations()).containsExactly(method.getAnnotations());

            String[] methodParameterNames = Stream.of(method.getParameters()).map(Parameter::getName)
                    .toArray(String[]::new);
            String[] proxyMethodParameterNames = Stream.of(proxyMethod.getParameters()).map(Parameter::getName)
                    .toArray(String[]::new);

            assertThat(proxyMethodParameterNames).containsExactly(methodParameterNames);
            assertThat(proxyMethod.getParameterTypes()).containsExactly(method.getParameterTypes());
            assertThat(proxyMethod.getGenericParameterTypes()).usingElementComparator(TestUtils.TYPE_COMPARATOR)
                    .containsExactly(method.getGenericParameterTypes());
            assertThat(proxyMethod.getParameterAnnotations()).containsExactly(method.getParameterAnnotations());
            assertThat(proxyMethod.getReturnType()).isEqualTo(method.getReturnType());
            assertThat(proxyMethod.getGenericReturnType()).usingComparator(TestUtils.TYPE_COMPARATOR)
                    .isEqualTo(method.getGenericReturnType());
            assertThat(proxyMethod.getExceptionTypes()).isEqualTo(method.getExceptionTypes());
            String[] wovenGenericExceptionTypes = Stream.of(proxyMethod.getGenericExceptionTypes())
                    .map(java.lang.reflect.Type::getTypeName)
                    .toArray(String[]::new);
            String[] genericExceptionTypes = Stream.of(method.getGenericExceptionTypes())
                    .map(java.lang.reflect.Type::getTypeName)
                    .toArray(String[]::new);
            assertThat(wovenGenericExceptionTypes).isEqualTo(genericExceptionTypes);
        }

    }

    @Test
    public void shouldWeaveMethodParameters() throws Exception {
        @SuppressWarnings("rawtypes")
        ProxyClass<GenericParamsService> proxyFactory = ProxyBuilder.build(proxyClassLoader,
                GenericParamsService.class,
                new Class<?>[0]);

        String[] wovenTypeParameters = Stream.of(proxyFactory.targetClass().getTypeParameters())
                .map(TypeVariable::getName)
                .toArray(String[]::new);
        String[] typeParameters = Stream.of(GenericParamsService.class.getTypeParameters()).map(TypeVariable::getName)
                .toArray(String[]::new);

        assertThat(wovenTypeParameters).containsExactly(typeParameters);

        ArrayList<MethodIdentifier> methodsToCompare = Lists.newArrayList(
                new MethodIdentifier("myMethod", Consumer.class),
                new MethodIdentifier("fooMaker"), new MethodIdentifier("unsafe"), new MethodIdentifier("unsafeGeneric"),
                new MethodIdentifier("consumeA", Object.class));
        for (MethodIdentifier methodId : methodsToCompare) {
            Method method = GenericParamsService.class.getMethod(methodId.name, methodId.parameterTypes);
            Method proxyMethod = proxyFactory.targetClass().getMethod(methodId.name, methodId.parameterTypes);

            assertThat(proxyMethod.getAnnotations()).containsExactly(method.getAnnotations());

            String[] methodParameterNames = Stream.of(method.getParameters()).map(Parameter::getName)
                    .toArray(String[]::new);
            String[] proxyMethodParameterNames = Stream.of(proxyMethod.getParameters()).map(Parameter::getName)
                    .toArray(String[]::new);

            assertThat(proxyMethodParameterNames).containsExactly(methodParameterNames);
            assertThat(proxyMethod.getParameterTypes()).containsExactly(method.getParameterTypes());
            assertThat(proxyMethod.getGenericParameterTypes()).containsExactly(method.getGenericParameterTypes());
            assertThat(proxyMethod.getParameterAnnotations()).containsExactly(method.getParameterAnnotations());
            assertThat(proxyMethod.getReturnType()).isEqualTo(method.getReturnType());
            assertThat(proxyMethod.getGenericReturnType()).isEqualTo(method.getGenericReturnType());
            assertThat(proxyMethod.getExceptionTypes()).isEqualTo(method.getExceptionTypes());
            String[] wovenGenericExceptionTypes = Stream.of(proxyMethod.getGenericExceptionTypes())
                    .map(java.lang.reflect.Type::getTypeName)
                    .toArray(String[]::new);
            String[] genericExceptionTypes = Stream.of(method.getGenericExceptionTypes())
                    .map(java.lang.reflect.Type::getTypeName)
                    .toArray(String[]::new);
            assertThat(wovenGenericExceptionTypes).isEqualTo(genericExceptionTypes);
        }

    }
}
