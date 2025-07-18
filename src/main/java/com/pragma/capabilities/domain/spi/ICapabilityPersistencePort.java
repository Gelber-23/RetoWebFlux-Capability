package com.pragma.capabilities.domain.spi;

import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.page.PageModel;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import reactor.core.publisher.Mono;

public interface ICapabilityPersistencePort {
    Mono<Capability> save(Capability capability);
    Mono<Capability> findById(Long id);
    Mono<PageModel<Capability>> getCapabilities(PageRequestModel pageRequest);
}
