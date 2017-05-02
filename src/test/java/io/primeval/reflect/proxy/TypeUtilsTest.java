package io.primeval.reflect.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.primeval.reflect.proxy.bytecode.TypeUtils;
import io.primeval.reflect.proxy.testset.api.generic.GenericService;
import io.primeval.reflect.proxy.testset.simplest.SimplestService;

public class TypeUtilsTest {

    @Test
    public void shouldReturnNullForNonGeneric() {
        String actual = TypeUtils.getTypeSignature(SimplestService.class);

        assertThat(actual).isNull();
    }

    @Test
    public void shouldGetGenericSignature() {
        String actual = TypeUtils.getTypeSignature(GenericService.class);

        String expected = "Ljava/lang/Object;"
                + "Lio/primeval/aspecio/internal/weaving/testset/api/GenericInterface"
                + "<Ljava/lang/Object;Ljava/lang/String;>;";

        assertThat(actual).isEqualTo(expected);
    }

}
