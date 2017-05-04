package io.primeval.reflex.proxy.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.primeval.reflex.proxy.bytecode.gen.BytecodeGenUtils;
import io.primeval.reflex.proxy.testset.api.generic.GenericService;
import io.primeval.reflex.proxy.testset.simplest.SimplestService;

public class BytecodeGenUtilsTest {

    @Test
    public void shouldReturnNullForNonGeneric() {
        String actual = BytecodeGenUtils.getTypeSignature(SimplestService.class);

        assertThat(actual).isNull();
    }

    @Test
    public void shouldGetGenericSignature() {
        String actual = BytecodeGenUtils.getTypeSignature(GenericService.class);

        String expected = "Ljava/lang/Object;Lio/primeval/reflex/proxy/testset/api/GenericInterface<Ljava/lang/Object;Ljava/lang/String;>;";

        assertThat(actual).isEqualTo(expected);
    }

}
