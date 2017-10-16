package io.primeval.reflex.service.adt;

public abstract class ADTInfo {

    public abstract Class<?> rootType();

    public abstract String typeName(Class<?> clazz);

    public abstract Class<?> classFor(String typeName);
    
    public abstract String selectorName();

}
