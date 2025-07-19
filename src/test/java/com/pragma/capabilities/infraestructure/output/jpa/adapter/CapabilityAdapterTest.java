package com.pragma.capabilities.infraestructure.output.jpa.adapter;


import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.CapabilityWithCount;
import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import com.pragma.capabilities.domain.model.web.Technology;
import com.pragma.capabilities.domain.spi.web.ITechnologyClientPort;
import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityEntity;
import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityTechnologyEntity;
import com.pragma.capabilities.infraestructure.output.jpa.mapper.ICapabilityEntityMapper;
import com.pragma.capabilities.infraestructure.output.jpa.repository.ICapabilityRepository;
import com.pragma.capabilities.infraestructure.output.jpa.repository.ICapabilityTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapabilityAdapterTest {

    @Mock
    private ICapabilityRepository capabilityRepository;
    @Mock
    private ICapabilityTechnologyRepository capabilityTechnologyRepository;
    @Mock
    private ICapabilityEntityMapper capabilityEntityMapper;
    @Mock
    private ITechnologyClientPort technologyClientPort;

    @InjectMocks
    private CapabilityAdapter capabilityAdapter;
    private final Capability capability = new Capability(1L, "Name", "Desc", List.of(
            new Technology(1L, "Tech1"),
            new Technology(2L, "Tech2"),
            new Technology(3L, "Tech3")
    ));

    private final CapabilityEntity capabilityEntity = new CapabilityEntity(1L, "Name", "Desc");

    @Test
    void save_ShouldReturnSavedCapability() {
        when(capabilityEntityMapper.toEntity(capability)).thenReturn(capabilityEntity);
        when(capabilityRepository.save(capabilityEntity)).thenReturn(Mono.just(capabilityEntity));
        when(capabilityTechnologyRepository.deleteAllByCapabilityId(1L)).thenReturn(Mono.empty());
        when(technologyClientPort.getTechnologiesByIds(any()))
                .thenReturn(Flux.fromIterable(capability.getTechnologies()));
        when(capabilityEntityMapper.toModel(capabilityEntity))
                .thenReturn(new Capability(1L, "Name", "Desc", capability.getTechnologies()));

        StepVerifier.create(capabilityAdapter.save(capability))
                .assertNext(result -> {
                    assertEquals(3, result.getTechnologies().size());
                    assertEquals("Name", result.getName());
                })
                .verifyComplete();

        verify(capabilityRepository).save(capabilityEntity);
        verify(capabilityTechnologyRepository).deleteAllByCapabilityId(1L);
        verify(technologyClientPort).getTechnologiesByIds(any());
    }

    @Test
    void findById_ShouldReturnCapability() {
        when(capabilityRepository.findById(1L)).thenReturn(Mono.just(capabilityEntity));
        when(capabilityTechnologyRepository.findAllByCapabilityId(1L)).thenReturn(Flux.just(
                new CapabilityTechnologyEntity(1L, 1L, 1L),
                new CapabilityTechnologyEntity(2L, 1L, 2L)
        ));
        when(technologyClientPort.getTechnologiesByIds(any()))
                .thenReturn(Flux.just(
                        new Technology(1L, "Tech1"),
                        new Technology(2L, "Tech2")
                ));
        when(capabilityEntityMapper.toModel(capabilityEntity)).thenReturn(new Capability(1L, "Name", "Desc", List.of()));

        StepVerifier.create(capabilityAdapter.findById(1L))
                .expectNextMatches(result -> result.getTechnologies().size() == 2)
                .verifyComplete();

        verify(capabilityRepository).findById(1L);
        verify(capabilityTechnologyRepository).findAllByCapabilityId(1L);
    }

    @Test
    void getCapabilities_ShouldReturnPage() {
        PageRequestModel pageRequest = new PageRequestModel(0, 10, SortOrder.ASC , SortBy.NAME);

        CapabilityWithCount projection = mock(CapabilityWithCount.class);
        when(projection.getId()).thenReturn(1L);
        when(projection.getName()).thenReturn("Name");
        when(projection.getDescription()).thenReturn("Desc");


        when(capabilityRepository.countAll()).thenReturn(Mono.just(1L));
        when(capabilityRepository.findPageOrderByNameAsc(10, 0L)).thenReturn(Flux.just(projection));

        when(capabilityTechnologyRepository.findAllByCapabilityId(1L))
                .thenReturn(Flux.just(new CapabilityTechnologyEntity(1L, 1L, 1L)));
        when(technologyClientPort.getTechnologiesByIds(any()))
                .thenReturn(Flux.just(new Technology(1L, "Tech1")));
        when(capabilityEntityMapper.toModel(any())).thenReturn(new Capability(1L, "Name", "Desc", List.of()));

        StepVerifier.create(capabilityAdapter.getCapabilities(pageRequest))
                .expectNextMatches(page -> page.getContent().size() == 1 && page.getTotalElements() == 1L)
                .verifyComplete();

        verify(capabilityRepository).findPageOrderByNameAsc(10, 0L);
        verify(capabilityRepository).countAll();
    }
}