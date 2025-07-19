package com.pragma.capabilities.domain.usecase;


import com.pragma.capabilities.domain.exception.InvalidCapabilityException;
import com.pragma.capabilities.domain.exception.TechnologyNotExitException;
import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import com.pragma.capabilities.domain.model.page.PageModel;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import com.pragma.capabilities.domain.model.web.Technology;
import com.pragma.capabilities.domain.spi.ICapabilityPersistencePort;
import com.pragma.capabilities.domain.spi.web.ITechnologyClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapabilityUseCaseTest {
    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;
    @Mock
    private ITechnologyClientPort technologyClientPort;
    @InjectMocks
    private CapabilityUseCase capabilityUseCase;

    private final Capability validCapability = new Capability(
            1L, "ValidName", "ValidDesc",  List.of(
            new Technology(1L, "Tech1"),
            new Technology(2L, "Tech2"),
            new Technology(3L, "Tech3")
    ));

    @Test
    void createCapability_ShouldSave_WhenValid() {
        when(technologyClientPort.getTechnologiesByIds(any()))
                .thenReturn(Flux.fromIterable(validCapability.getTechnologies()));
        when(capabilityPersistencePort.save(any()))
                .thenReturn(Mono.just(validCapability));

        StepVerifier.create(capabilityUseCase.createCapability(validCapability))
                .expectNextMatches(c -> c.getName().equals(validCapability.getName()))
                .verifyComplete();

        verify(capabilityPersistencePort).save(any());
    }

    @Test
    void createCapability_ShouldError_WhenInvalidData() {
        Capability invalid = new Capability(1L, "", "", List.of());
        StepVerifier.create(capabilityUseCase.createCapability(invalid))
                .expectError(InvalidCapabilityException.class)
                .verify();
        verify(capabilityPersistencePort, never()).save(any());
    }

    @Test
    void createCapability_ShouldError_WhenTechnologyNotFound() {
        when(technologyClientPort.getTechnologiesByIds(any()))
                .thenReturn(Flux.empty());

        StepVerifier.create(capabilityUseCase.createCapability(validCapability))
                .expectError(TechnologyNotExitException.class)
                .verify();
        verify(capabilityPersistencePort, never()).save(any());
    }

    @Test
    void getCapabilityById_ShouldReturn() {
        when(capabilityPersistencePort.findById(1L)).thenReturn(Mono.just(validCapability));
        StepVerifier.create(capabilityUseCase.getCapabilityById(1L))
                .expectNext(validCapability)
                .verifyComplete();
    }

    @Test
    void getCapabilities_ShouldReturnPage() {
        PageModel<Capability> page = new PageModel<>(List.of(validCapability), 1L,1,1);
        when(capabilityPersistencePort.getCapabilities(any())).thenReturn(Mono.just(page));
        StepVerifier.create(capabilityUseCase.getCapabilities(new PageRequestModel(0, 10, SortOrder.ASC,SortBy.NAME)))
                .expectNext(page)
                .verifyComplete();
    }

    @Test
    void createCapability_ShouldError_WhenDuplicateTechnologies() {
        Technology tech = new Technology(1L, "Tech");
        Capability cap = new Capability(1L, "Name", "Desc", List.of(tech, tech));
        StepVerifier.create(capabilityUseCase.createCapability(cap))
                .expectError(InvalidCapabilityException.class)
                .verify();
        verify(capabilityPersistencePort, never()).save(any());
    }

}