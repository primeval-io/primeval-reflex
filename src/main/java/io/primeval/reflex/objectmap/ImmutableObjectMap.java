package io.primeval.reflex.objectmap;

import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ImmutableObjectMap implements Map<String, Object> {

    private final Object o;
    private final FieldMap fieldMap;
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public ImmutableObjectMap(Object o, FieldMap fieldMap) {
        this.o = o;
        this.fieldMap = fieldMap;
    }

    @Override
    public int size() {
        return fieldMap.size();
    }

    @Override
    public boolean isEmpty() {
        return fieldMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return fieldMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object k : keySet()) {
            Object object = get(k);
            if (Objects.equals(object, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        Field f = fieldMap.get(key);
        if (f != null) {
            return cache.computeIfAbsent((String) key, k -> {
                try {
                    return f.get(o);
                } catch (IllegalArgumentException | SecurityException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw new AssertionError("only public fields!");
                }
            });
        }
        return null;
    }

    @Override
    @Deprecated
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    @Deprecated
    public Object remove(Object key) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    @Deprecated
    public void putAll(Map<? extends String, ? extends Object> m) {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException("immutable");
    }

    @Override
    public Set<String> keySet() {
        return fieldMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return new AbstractCollection<Object>() {

            @Override
            public Iterator<Object> iterator() {
                return ImmutableObjectMap.this.keySet().stream().map(key -> ImmutableObjectMap.this.get(key))
                        .iterator();
            }

            @Override
            public int size() {
                return fieldMap.size();
            }

        };
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return new AbstractSet<Map.Entry<String, Object>>() {

            @Override
            public Iterator<java.util.Map.Entry<String, Object>> iterator() {
                return ImmutableObjectMap.this.keySet().stream()
                        .map(key -> createEntry(key, ImmutableObjectMap.this.get(key)))
                        .iterator();
            }

            @Override
            public int size() {
                return fieldMap.size();
            }

            private <A, B> Entry<A, B> createEntry(A key, B value) {
                return new SimpleImmutableEntry<>(key, value);
            }
        };
    }

}
