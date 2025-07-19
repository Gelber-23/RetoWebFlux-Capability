package com.pragma.capabilities.infraestructure.output.client;


import com.pragma.capabilities.domain.model.web.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyWebClientAdapterTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    private TechnologyWebClientAdapter adapter;

    @BeforeEach
    void setup() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        adapter = new TechnologyWebClientAdapter(webClientBuilder, "http://localhost:8080");
    }

    @Test
    void getTechnologiesByIds_ShouldReturnTechnologies() {
        when(webClient.get()
                .uri(anyString())
                .retrieve()
                .bodyToMono(Technology.class))
                .thenReturn(Mono.just(new Technology(1L, "Tech1")));

        StepVerifier.create(adapter.getTechnologiesByIds(Flux.just(1L)))
                .expectNextMatches(tech -> tech.getId().equals(1L))
                .verifyComplete();

        verify(webClient, atLeastOnce()).get();
        verify(webClient.get(), atLeastOnce()).uri("/1");
        verify(webClient.get().uri("/1"), atLeastOnce()).retrieve();
    }
}