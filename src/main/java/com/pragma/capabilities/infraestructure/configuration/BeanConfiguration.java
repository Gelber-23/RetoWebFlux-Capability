package com.pragma.capabilities.infraestructure.configuration;

import com.pragma.capabilities.domain.api.ICapabilityServicePort;
import com.pragma.capabilities.domain.spi.ICapabilityPersistencePort;
import com.pragma.capabilities.domain.usecase.CapabilityUseCase;
import com.pragma.capabilities.infraestructure.output.client.TechnologyWebClientAdapter;
import com.pragma.capabilities.infraestructure.output.jpa.adapter.CapabilityAdapter;
import com.pragma.capabilities.infraestructure.output.jpa.mapper.ICapabilityEntityMapper;
import com.pragma.capabilities.infraestructure.output.jpa.repository.ICapabilityRepository;
import com.pragma.capabilities.infraestructure.output.jpa.repository.ICapabilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final ICapabilityRepository capabilityRepository;
    private final ICapabilityEntityMapper capabilityEntityMapper;
    private final ICapabilityTechnologyRepository capabilityTechnologyRepository;
    private final TechnologyWebClientAdapter technologyWebClientAdapter;
    private final TransactionalOperator transactionalOperator;
    @Bean
    public ICapabilityPersistencePort capabilityPersistencePort(){
        return new CapabilityAdapter(capabilityRepository, capabilityTechnologyRepository, capabilityEntityMapper,technologyWebClientAdapter,transactionalOperator);
    }

    @Bean
    public ICapabilityServicePort capabilityServicePort(){
        return new CapabilityUseCase(capabilityPersistencePort(),technologyWebClientAdapter);
    }
}
