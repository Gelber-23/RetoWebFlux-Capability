package com.pragma.capabilities.infraestructure.input.res;


import com.pragma.capabilities.application.dto.request.CapabilityRequest;
import com.pragma.capabilities.application.dto.request.TechnologyRequest;
import com.pragma.capabilities.application.dto.response.CapabilityResponse;
import com.pragma.capabilities.application.dto.response.PageResponse;
import com.pragma.capabilities.application.dto.response.TechnologyResponse;
import com.pragma.capabilities.application.handler.ICapabilityHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityRestControllerTest {

    @Mock
    private ICapabilityHandler capabilityHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        CapabilityRestController controller = new CapabilityRestController(capabilityHandler);
        this.webTestClient = WebTestClient.bindToController(controller).build();
    }

    private final CapabilityRequest request = new CapabilityRequest("Name", "Desc", List.of(new TechnologyRequest(1L), new TechnologyRequest(2L), new TechnologyRequest(3L)));
    private final CapabilityResponse response = new CapabilityResponse(1L, "Name", "Desc",  List.of(new TechnologyResponse(1L,"1"),new TechnologyResponse(1L,"1"),new TechnologyResponse(1L,"1")));
    private final PageResponse<CapabilityResponse> pageResponse = new PageResponse<>(List.of(response), 1L, 0, 10, 1);

    @Test
    void createCapability_ShouldReturnOk() {
        when(capabilityHandler.createCapability(any()))
                .thenReturn(Mono.just(response));
        webTestClient.post()
                .uri("/capability/")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void getCapabilityById_ShouldReturnOk() {
        when(capabilityHandler.getCapabilityById(1L)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/capability/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Name");
    }

    @Test
    void getCapabilities_ShouldReturnOk() {
        when(capabilityHandler.getCapabilities(any())).thenReturn(Mono.just(pageResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/capability/")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .queryParam("order", "DESC")
                        .queryParam("sortBy", "NAME")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalElements").isEqualTo(1);
    }
}