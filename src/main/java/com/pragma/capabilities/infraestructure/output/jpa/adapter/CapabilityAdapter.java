package com.pragma.capabilities.infraestructure.output.jpa.adapter;


import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.CapabilityWithCount;
import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import com.pragma.capabilities.domain.model.page.PageModel;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import com.pragma.capabilities.domain.model.web.Technology;
import com.pragma.capabilities.domain.spi.ICapabilityPersistencePort;
import com.pragma.capabilities.domain.spi.web.ITechnologyClientPort;
import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityEntity;
import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityTechnologyEntity;
import com.pragma.capabilities.infraestructure.output.jpa.mapper.ICapabilityEntityMapper;
import com.pragma.capabilities.infraestructure.output.jpa.repository.ICapabilityRepository;
import com.pragma.capabilities.infraestructure.output.jpa.repository.ICapabilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;


@RequiredArgsConstructor
public class CapabilityAdapter implements ICapabilityPersistencePort {

    private final ICapabilityRepository capabilityRepository;
    private final ICapabilityTechnologyRepository capabilityTechnologyRepository;
    private final ICapabilityEntityMapper capabilityEntityMapper;
    private final ITechnologyClientPort technologyClientPort;
    private final TransactionalOperator transactionalOperator;
    @Override
    public Mono<Capability> save(Capability capability) {

        return capabilityRepository
                .save(capabilityEntityMapper.toEntity(capability))
                .flatMap(savedEntity -> {
                    Long capId = savedEntity.getId();
                    return capabilityTechnologyRepository
                            .deleteAllByCapabilityId(capId)

                            .thenMany(Flux.fromIterable(capability.getTechnologies()))
                            .map(t -> new CapabilityTechnologyEntity(null, capId, t.getId()))
                            .flatMap(capabilityTechnologyRepository::save)

                            .thenMany(Flux.fromIterable(capability.getTechnologies()).map(Technology::getId))
                            .as(technologyClientPort::getTechnologiesByIds)
                            .collectList()

                            .map(fullTechs -> {
                                Capability capModel = capabilityEntityMapper.toModel(savedEntity);
                                capModel.setTechnologies(fullTechs);
                                return capModel;
                            });
                });
    }

    @Override
    public Mono<Capability> findById(Long id) {
        return capabilityRepository.findById(id)
                .flatMap(entity ->

                        capabilityTechnologyRepository.findAllByCapabilityId(id)

                                .map(CapabilityTechnologyEntity::getTechnologyId)
                                .collectList()

                                .flatMap(ids ->
                                        technologyClientPort.getTechnologiesByIds(Flux.fromIterable(ids))
                                                .collectList()
                                                .map(fullTechs -> {
                                                    Capability capModel = capabilityEntityMapper.toModel(entity);
                                                    capModel.setTechnologies(fullTechs);
                                                    return capModel;
                                                })
                                )
                );
    }

    @Override
    public Mono<PageModel<Capability>> getCapabilities(PageRequestModel pageRequest) {
        int    page      = pageRequest.getPage();
        int    size      = pageRequest.getSize();
        long   offset    = (long) page * size;

        Mono<Long> total = capabilityRepository.countAll();


        Flux<CapabilityWithCount> pageFlux = switch (pageRequest.getSortBy()) {
            case SortBy.NAME -> pageRequest.getOrder() == SortOrder.ASC
                    ? capabilityRepository.findPageOrderByNameAsc(size, offset)
                    : capabilityRepository.findPageOrderByNameDesc(size, offset);
            case SortBy.TECH_COUNT -> pageRequest.getOrder() == SortOrder.ASC
                    ? capabilityRepository.findPageOrderByCountAsc(size, offset)
                    : capabilityRepository.findPageOrderByCountDesc(size, offset);
        };


        Mono<List<Capability>> content = pageFlux
                .flatMap(proj -> {
                    CapabilityEntity ent = new CapabilityEntity();

                    ent.setId(proj.getId());
                    ent.setName(proj.getName());
                    ent.setDescription(proj.getDescription());

                    Capability cap = capabilityEntityMapper.toModel(ent);

                    return capabilityTechnologyRepository.findAllByCapabilityId(proj.getId())
                            .map(CapabilityTechnologyEntity::getTechnologyId)
                            .collectList()
                            .flatMapMany(ids -> technologyClientPort.getTechnologiesByIds(Flux.fromIterable(ids)))
                            .collectList()
                            .map(techs -> {
                                cap.setTechnologies(techs);
                                return cap;
                            });
                })
                .collectList();


        return Mono.zip(total, content)
                .map(tp -> {
                    long tot = tp.getT1();
                    List<Capability> caps = tp.getT2();

                    return new PageModel<>(caps, tot, page, size);
                });
    }
    @Override
    public Mono<Void> deleteCapabilityById(Long capabilityId) {

        return capabilityTechnologyRepository.findAllByCapabilityId(capabilityId)
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .collectList()

                .flatMap(techIds ->

                        Flux.fromIterable(techIds)
                                .flatMap(techId ->
                                        capabilityTechnologyRepository.findAllByTechnologyId(techId)
                                                .count()
                                                .filter(count -> count == 1)
                                                .map(id -> techId)
                                )
                                .collectList()

                                .flatMap(techIdsToDelete ->

                                        transactionalOperator
                                                .execute(status ->
                                                        capabilityRepository.deleteById(capabilityId)
                                                )
                                                .then(

                                                        Flux.fromIterable(techIdsToDelete)
                                                                .flatMap(technologyClientPort::deleteTechnologyById)
                                                                .then()
                                                )
                                )
                );
    }



}
