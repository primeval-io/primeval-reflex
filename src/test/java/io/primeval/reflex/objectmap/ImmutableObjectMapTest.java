package io.primeval.reflex.objectmap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import io.primeval.reflex.Reflex;

public class ImmutableObjectMapTest {

    @Test
    public void test() {
        Foo foo = new Foo("bar", 42);
        Map<String, Object> map = Reflex.asMap(foo);
        assertThat(map.get("bar")).isEqualTo("bar");
        assertThat(map.get("baz")).isEqualTo(42);
        assertThat(map.get("booz")).isNull();
        
        assertThat(map.get("bar")).isEqualTo("bar");
        assertThat(map.get("baz")).isEqualTo(42);
        assertThat(map.get("booz")).isNull();
    }

}
