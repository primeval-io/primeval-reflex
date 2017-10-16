package io.primeval.reflex.service.adt.internal;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import io.primeval.reflex.service.adt.ADTInfo;
import io.primeval.reflex.service.adt.ADTMirror;
import io.primeval.reflex.service.adt.spi.ADTMirrorProvider;

@Component(immediate = true)
public final class ADTMirrorImpl implements ADTMirror {

    public final List<ADTMirrorProvider> providers = new CopyOnWriteArrayList<>();

    // Switch to evicting cache... need primeval-cache :-)
    // Cache only ADTTypes, and make ADT checking fast!
    private final Map<Class<?>, ADTInfo> adtCache = new ConcurrentHashMap<>();
    
    // A bloomfilter-like structure would be neat to keep failed ADT-checks without using too much mem
    
    // Right now excluding java.* types, should make this configurable?

    @Override
    public Optional<ADTInfo> getInfo(Class<?> raw) {
        if (raw.getName().startsWith("java.")) {
            return Optional.empty();
        }
        return Optional.ofNullable(adtCache.computeIfAbsent(raw, k -> computeADTInfo(k)));
    }

    private ADTInfo computeADTInfo(Class<?> raw) {
        for (ADTMirrorProvider prov : providers) {
            if (prov.isADT(raw)) {
                ADTInfo info = computeADTInfoForProvider(prov, raw);
                return info;
            }
        }
        return null;
    }

    private ADTInfo computeADTInfoForProvider(ADTMirrorProvider prov, Class<?> raw) {
        return prov.getInfo(raw,
                k -> adtCache.computeIfAbsent(k, k2 -> computeADTInfoForProvider(prov, k2)));
    }

    // Static policy to invalidate cache when a new provider joins!
    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY)
    public void addADTMirrorProvider(ADTMirrorProvider adtMirrorProvider) {
        providers.add(adtMirrorProvider);
    }

}
