package com.pragma.capabilities.domain.spi.web;

import com.pragma.capabilities.domain.model.web.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyClientPort {
    Flux<Technology> getTechnologiesByIds(Flux<Long> ids);
    Mono<Void> deleteTechnologyById(Long id);
}
