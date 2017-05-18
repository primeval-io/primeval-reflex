package io.primeval.reflex;

import java.util.Map;

import io.primeval.reflex.objectmap.FieldMap;
import io.primeval.reflex.objectmap.ImmutableObjectMap;

public final class Reflex {

    private Reflex() {

    }

    public static Map<String, Object> asMap(Object o) {
        return new ImmutableObjectMap(o, new FieldMap(o.getClass().getFields()));
    }

    public static Map<String, Object> asMap(Map<String, Object> m) {
        return m;
    }
}
