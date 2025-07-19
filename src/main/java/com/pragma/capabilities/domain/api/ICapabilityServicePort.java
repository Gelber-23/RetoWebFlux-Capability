package com.pragma.capabilities.domain.api;

import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.page.PageModel;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import reactor.core.publisher.Mono;

public interface ICapabilityServicePort {

    Mono<Capability> createCapability(Capability cap);
    Mono<Capability> getCapabilityById(Long id);
    Mono<PageModel<Capability>> getCapabilities(PageRequestModel pageRequest);
}
