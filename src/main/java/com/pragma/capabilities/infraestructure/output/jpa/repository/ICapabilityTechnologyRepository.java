package com.pragma.capabilities.infraestructure.output.jpa.repository;

import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapabilityTechnologyRepository extends ReactiveCrudRepository<CapabilityTechnologyEntity, Long> {
    Flux<CapabilityTechnologyEntity> findAllByCapabilityId(Long capabilityId);
    Mono<Void> deleteAllByCapabilityId(Long capabilityId);
}