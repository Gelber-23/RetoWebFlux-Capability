package com.pragma.capabilities.application.handler;

import com.pragma.capabilities.application.dto.request.CapabilityRequest;
import com.pragma.capabilities.application.dto.request.PageRequest;
import com.pragma.capabilities.application.dto.response.CapabilityResponse;
import com.pragma.capabilities.application.dto.response.PageResponse;
import reactor.core.publisher.Mono;

public interface ICapabilityHandler {

    Mono<CapabilityResponse> createCapability(CapabilityRequest cap);
    Mono<CapabilityResponse> getCapabilityById(Long id);
    Mono<PageResponse<CapabilityResponse>> getCapabilities(PageRequest pageRequest);
}
