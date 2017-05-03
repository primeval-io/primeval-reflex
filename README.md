# primeval-reflect [![Build Status](https://travis-ci.org/primeval-io/primeval-reflect.svg?branch=master)](https://travis-ci.org/primeval-io/primeval-reflect) 

primeval-reflect is a Java reflection library, currently providing an improved JDK Proxy. Additional utilities might be added later. It is OSGi compatible, but it can be used within any Java application.

# Maven coordinates


```xml

	<groupId>io.primeval</groupId>
	<artifactId>primeval-reflect</artifactId>
	<version>1.0.0-SNAPSHOT</version>
```

Until a stable release (soon), the snapshot version is available in the Sonatype OSS Snapshots repository.


# Dependencies

primeval-reflects requires Java 8 and depends on the SLF4J logging API and the ASM bytecode manipulation library. 

```xml
	<dependency>
		<groupId>org.slf4j</groupId>
		<artifactId>slf4j-api</artifactId>
		<version>1.7.12</version>
		<scope>provided</scope>
	</dependency>


	<dependency>
		<groupId>org.ow2.asm</groupId>
		<artifactId>asm</artifactId>
		<version>5.2</version>
	</dependency>
```

# Proxy features

* __Smart defaults:__
  * All public methods not from `java.lang.Object` are proxied;
  * Simple yet extensive Interception API;
  * Intercepted Arguments are immutable and cacheable thanks to implementations of `Object#equals` and `Object#hashCode`.
* __Framework-friendly:__
  * Annotations are proxied, so framework can introspect them;
  * Same goes for generic signatures!
  * And method parameter names (from Java 8's `javac -parameters` option, or synthetic parameter names); 
  * Just a library that will integrate in any Java application.
* __Fast:__ 
  * Generated classes are minimal;
  * Class generation is done using ASM core library only to avoid any overhead ; 
  * Primitive types are not boxed, but Interceptors have default methods to box primitives to make righting interceptors quickly less tedious;
  * Simple API to decide which methods are intercepted; those which are not are directly delegated with almost zero overhead.
* __Versatile:__
  * Interceptors can do whatever they like, included retrying after an exception, delaying the call, block, etc
  * Interceptors can be stacked
  * Specialized AnnotationInterceptors allow interception to provide the matching annotation, works with repeatable annotations;
* __Dynamic:__
  * A proxy can change interceptors dynamically.
  * OSGi-compatible; [Aspecio](https://github.com/primeval-io/aspecio) provides OSGi AOP Proxy aspects support using primeval-proxies 
   
   
# Usage

Imagine you want to proxy the following code.
```java

interface Hello {

	String getHello(String name);
}

final class HelloImpl implements Hello {

	@Override
	public String getHello(String name) {
		return "Hello " + name;
	}

}

```

We can proxy it this way.

```java

	ProxyClass<HelloImpl> proxyClass = ProxyBuilder.build(HelloImpl.class, new Class<?>[] { Hello.class });

	HelloImpl helloImpl = new HelloImpl();
        
	Proxy proxy = proxyClass.newInstance(helloImpl);
       
	Hello helloProxy = (Hello) proxy;
	String helloMsg = helloProxy.getHello("world");
        
	System.out.println(helloMsg);            
```

This prints `Hello world`. The call was successfully proxied, but we did not intercept it. Primeval-Reflect proxies support a dynamic change of interceptors, and are always created with the `Interceptor.DEFAULT` interceptor, that simply delegates the original call.

To set an interceptor, we first have to create one. Let us make one add an exclamation mark (!) at the end of the returned message. Normally, interceptors are very generic, but for the sake of the demo we will expect the method we intercept returns a `String`. Keep in mind that the same interceptor will be called for _all_ intercepted methods (by default, all of the proxy's methods that are not inherited of `java.lang.Object`).

```java

	Interceptor exclamationMarkInterceptor = new Interceptor() {

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
        
	proxy.setInterceptor(exclamationMarkInterceptor);
	
	String helloMsg = helloProxy.getHello("world");
	System.out.println(helloMsg);

```

This prints `Hello world!`. This showed how to change a result. We could also decide not to call `handler.invoke()` at all, or call it several times. It's up to us! Now, what if we wanted to change the arguments? We can create another interceptor for this:

```java

	Interceptor universeInterceptor = new Interceptor() {

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
	proxy.setInterceptor(universeInterceptor);
	
	String helloMsg = helloProxy.getHello("world");
	System.out.println(helloMsg);
```

Now, this printed `Hello world^Wuniverse`. When we want to change arguments, even though we can implement our own class implementing `Arguments`, it is better (and faster for the proxies!) to use the fluent `Argument#udpdate()` API. Arguments are always matched with their name, but when using arguments built by the library, they convert to final fields, and the `invoke(Arguments)` method will directly get those fields. If you don't change arguments at all, it is even slightly faster to call the `invoke()` method that won't check if it's the fast generated class.

So far so good! In case of exceptions we could simply use `try`/`catch`/`finally` blocks around `handler.invoke()`. We could throw at any time. 

What if we wanted our two interceptors to be active? Nothing's easier ;)

```java 	
	Interceptor composedInterceptor = Interceptors.compose(universeInterceptor, exclamationMarkInterceptor);
	proxy.setInterceptor(exclamationMarkInterceptor);
	
	String helloMsg = helloProxy.getHello("world");
	System.out.println(helloMsg);
```

As expected, this printed `Hello world^Wuniverse!`. Composing interceptors stack them in the order you'd expect, and each of them has its full capabilities.


# Dealing with primitive types

If you want to avoid primitive boxing, there are alternative `Interceptor#onCall` methods for each primitive type. If you don't redefine them, they default to calling the object version with a boxed value.

For instance, to intercept a method with return type `int` without boxing, one needs to define the following in the interceptor:

```java

	public int onCall(CallContext callContext, IntInterceptionHandler handler) throws Exception {
		if (callContext.method.getName().equals("increase")) {		
			return handler.invoke() * 3;
		}
		return handler.invoke();
	}
           
```

 
# Intercepting annotations

This example demonstrates annotated method interception. 

```java 

@Retention(RetentionPolicy.RUNTIME)
public @interface OverrideReturn {

    String value();
}


public final class AnnotatedHello implements Hello {
    @Override
    @OverrideReturn("Goodbye!")
    public String getHello(String name) {
        return "Hello " + name;
    }
}
```
The annotation must have runtime retention. To intercept it, we can use the following code:

```java

	ProxyClass<AnnotatedHello> proxyClass = ProxyBuilder.build(AnnotatedHello.class, Class<?>[] { Hello.class });

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
```

This prints `Goodbye!`.

`AnnotationInterceptor` extends `Interceptor` : all that we've seen previously applies, including composition. In case of repeated annotations, the `onCall` method will be called several times for each repeated annotation, in a stacked manner.


# Getting help

Post a new GitHub issue or join on https://gitter.im/primeval-io/Lobby [![Gitter](https://badges.gitter.im/primeval-io/Lobby.svg)](https://gitter.im/primeval-io/Lobby).
 


# Author

primeval-reflect was developed by Simon Chemouil.

# Copyright

(c) 2016-2017, Simon Chemouil, Lambdacube, Primeval