package io.primeval.reflex.service.adt;

import java.util.Optional;

public interface ADTMirror {

	Optional<ADTInfo> getInfo(Class<?> raw);


}
