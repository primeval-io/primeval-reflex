package io.primeval.reflex.service.adt.spi;

import java.util.function.Function;

import io.primeval.reflex.service.adt.ADTInfo;

public interface ADTMirrorProvider {

    ADTInfo getInfo(Class<?> raw, Function<Class<?>, ADTInfo> callback);

    boolean isADT(Class<?> raw);
}
