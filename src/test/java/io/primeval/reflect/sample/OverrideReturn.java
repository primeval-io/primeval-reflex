package io.primeval.reflect.sample;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OverrideReturn {

    String value();
}
