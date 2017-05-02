package io.primeval.reflect.proxy.testset.api;

public interface GenericInterface<A, B extends A> {

    B makeB();
    
    void consumeA(A boo);
    
    default void doSome() {
        consumeA(makeB());
    }
}
