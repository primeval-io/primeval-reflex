package io.primeval.reflex.objectmap;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class FieldMap implements Map<String, Field> {

    private final Map<String, Field> fieldMap;

    public FieldMap(Field[] fields) {
        Map<String, Field> bldr = new HashMap<>();
        for (Field f : fields) {
            bldr.put(f.getName(), f);
        }
        this.fieldMap = Collections.unmodifiableMap(bldr);
    }

    public int size() {
        return fieldMap.size();
    }

    public boolean isEmpty() {
        return fieldMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return fieldMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return fieldMap.containsValue(value);
    }

    public Field get(Object key) {
        return fieldMap.get(key);
    }

    public Field put(String key, Field value) {
        return fieldMap.put(key, value);
    }

    public Field remove(Object key) {
        return fieldMap.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Field> m) {
        fieldMap.putAll(m);
    }

    public void clear() {
        fieldMap.clear();
    }

    public Set<String> keySet() {
        return fieldMap.keySet();
    }

    public Collection<Field> values() {
        return fieldMap.values();
    }

    public Set<java.util.Map.Entry<String, Field>> entrySet() {
        return fieldMap.entrySet();
    }

    public boolean equals(Object o) {
        return fieldMap.equals(o);
    }

    public int hashCode() {
        return fieldMap.hashCode();
    }

}
