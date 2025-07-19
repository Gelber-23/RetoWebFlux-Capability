package com.pragma.capabilities.application.handler.impl;


import com.pragma.capabilities.application.dto.request.CapabilityRequest;
import com.pragma.capabilities.application.dto.request.PageRequest;
import com.pragma.capabilities.application.dto.request.TechnologyRequest;
import com.pragma.capabilities.application.dto.response.CapabilityResponse;
import com.pragma.capabilities.application.dto.response.TechnologyResponse;
import com.pragma.capabilities.application.mapper.request.ICapabilityRequestMapper;
import com.pragma.capabilities.application.mapper.request.IPageRequestMapper;
import com.pragma.capabilities.application.mapper.response.ICapabilityResponseMapper;
import com.pragma.capabilities.domain.api.ICapabilityServicePort;
import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import com.pragma.capabilities.domain.model.page.PageModel;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityHandlerTest {

    @Mock
    private ICapabilityServicePort capabilityServicePort;
    @Mock
    private ICapabilityRequestMapper capabilityRequestMapper;
    @Mock
    private ICapabilityResponseMapper capabilityResponseMapper;
    @Mock
    private IPageRequestMapper pageRequestMapper;

    @InjectMocks
    private CapabilityHandler capabilityHandler;

    private final CapabilityRequest request = new CapabilityRequest("Name", "Desc", List.of(new TechnologyRequest(1L), new TechnologyRequest(2L), new TechnologyRequest(3L)));
    private final Capability model = new Capability(1L, "Name", "Desc", List.of());
    private final CapabilityResponse response = new CapabilityResponse(1L, "Name", "Desc",  List.of(new TechnologyResponse(1L,"1"),new TechnologyResponse(1L,"1"),new TechnologyResponse(1L,"1")));
    private final PageRequest pageRequest = new PageRequest(0, 10, SortOrder.DESC, SortBy.NAME);
    private final PageRequestModel pageRequestModel = new PageRequestModel(0, 10, SortOrder.DESC, SortBy.NAME);
    private final PageModel<Capability> pageModel = new PageModel<>(List.of(model), 1L, 0, 10);

    @Test
    void createCapability_ShouldReturnResponse() {
        when(capabilityRequestMapper.toModel(request)).thenReturn(model);
        when(capabilityServicePort.createCapability(model)).thenReturn(Mono.just(model));
        when(capabilityResponseMapper.toResponse(model)).thenReturn(response);

        StepVerifier.create(capabilityHandler.createCapability(request))
                .expectNext(response)
                .verifyComplete();

        verify(capabilityServicePort).createCapability(model);
    }

    @Test
    void getCapabilityById_ShouldReturnResponse() {
        when(capabilityServicePort.getCapabilityById(1L)).thenReturn(Mono.just(model));
        when(capabilityResponseMapper.toResponse(model)).thenReturn(response);

        StepVerifier.create(capabilityHandler.getCapabilityById(1L))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void getCapabilities_ShouldReturnPageResponse() {
        when(pageRequestMapper.toModel(pageRequest)).thenReturn(pageRequestModel);
        when(capabilityServicePort.getCapabilities(pageRequestModel)).thenReturn(Mono.just(pageModel));
        when(capabilityResponseMapper.toResponse(model)).thenReturn(response);

        StepVerifier.create(capabilityHandler.getCapabilities(pageRequest))
                .expectNextMatches(pageResp ->
                        pageResp.getContent().size() == 1 &&
                                pageResp.getTotalElements() == 1L &&
                                pageResp.getPageNumber() == 0 &&
                                pageResp.getPageSize() == 10 &&
                                pageResp.getTotalPages() == 1 &&
                                pageResp.getContent().getFirst().getId().equals(1L)
                )
                .verifyComplete();
    }
}