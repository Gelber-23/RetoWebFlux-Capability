package com.pragma.capabilities.application.handler.impl;

import com.pragma.capabilities.application.dto.request.CapabilityRequest;
import com.pragma.capabilities.application.dto.request.PageRequest;
import com.pragma.capabilities.application.dto.response.CapabilityResponse;
import com.pragma.capabilities.application.dto.response.PageResponse;
import com.pragma.capabilities.application.handler.ICapabilityHandler;
import com.pragma.capabilities.application.mapper.request.ICapabilityRequestMapper;
import com.pragma.capabilities.application.mapper.request.IPageRequestMapper;
import com.pragma.capabilities.application.mapper.response.ICapabilityResponseMapper;
import com.pragma.capabilities.domain.api.ICapabilityServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CapabilityHandler implements ICapabilityHandler {

    private final ICapabilityServicePort capabilityServicePort;
    private final ICapabilityRequestMapper capabilityRequestMapper;
    private final ICapabilityResponseMapper capabilityResponseMapper;
    private final IPageRequestMapper pageRequestMapper;

    @Override
    public Mono<CapabilityResponse> createCapability(CapabilityRequest cap) {
        return Mono.just(cap)
                .map(capabilityRequestMapper::toModel)
                .flatMap(capabilityServicePort::createCapability)
                .map(capabilityResponseMapper::toResponse);


    }

    @Override
    public Mono<CapabilityResponse> getCapabilityById(Long id) {
        return capabilityServicePort.getCapabilityById(id)
                .map(capabilityResponseMapper::toResponse);
    }

    @Override
    public Mono<PageResponse<CapabilityResponse>> getCapabilities(PageRequest pageRequest) {
        return capabilityServicePort.getCapabilities(pageRequestMapper.toModel(pageRequest))
                .map(pageModel -> {

                    List<CapabilityResponse> listCapability = pageModel.getContent().stream()
                            .map(capabilityResponseMapper::toResponse)
                            .toList();

                    return new PageResponse<>(
                            listCapability,
                            pageModel.getTotalElements(),
                            pageModel.getPageNumber(),
                            pageModel.getPageSize(),
                            pageModel.getTotalPages()
                    );
                });
    }

    @Override
    public Mono<Void> deleteCapabilityById(Long id) {
        return capabilityServicePort.deleteCapabilityById(id);
    }
}
