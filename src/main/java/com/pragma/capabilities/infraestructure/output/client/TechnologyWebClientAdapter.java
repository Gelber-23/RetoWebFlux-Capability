package com.pragma.capabilities.infraestructure.output.client;

import com.pragma.capabilities.domain.model.web.Technology;
import com.pragma.capabilities.domain.spi.web.ITechnologyClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
@Component
public class TechnologyWebClientAdapter  implements ITechnologyClientPort {

    private final WebClient webClient;

    public TechnologyWebClientAdapter(WebClient.Builder builder, @Value("${client.technology}") String technologyUrl) {


        this.webClient = builder.baseUrl(technologyUrl).build();
    }

    @Override
    public Flux<Technology> getTechnologiesByIds(Flux<Long> ids) {
        return ids.flatMap(id ->
                webClient.get()
                        .uri("/" + id)
                        .retrieve()
                        .bodyToMono(Technology.class)
        );
    }
}
