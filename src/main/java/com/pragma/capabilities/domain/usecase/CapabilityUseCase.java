package com.pragma.capabilities.domain.usecase;

import com.pragma.capabilities.domain.api.ICapabilityServicePort;
import com.pragma.capabilities.domain.exception.InvalidCapabilityException;
import com.pragma.capabilities.domain.exception.TechnologyNotExitException;
import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.domain.model.page.PageModel;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import com.pragma.capabilities.domain.model.web.Technology;
import com.pragma.capabilities.domain.spi.ICapabilityPersistencePort;
import com.pragma.capabilities.domain.spi.web.ITechnologyClientPort;
import com.pragma.capabilities.domain.util.ExceptionConstans;
import com.pragma.capabilities.domain.util.ValueConstants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CapabilityUseCase implements ICapabilityServicePort {
    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final ITechnologyClientPort technologyClientPort;

    public CapabilityUseCase(ICapabilityPersistencePort capabilityPersistencePort, ITechnologyClientPort technologyClientPort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.technologyClientPort = technologyClientPort;
    }

    @Override
    public Mono<Capability> createCapability(Capability cap) {
        return validateData(cap)
                .flatMap(this::validateTechnologies)
                .flatMap(capabilityPersistencePort::save);

    }

    @Override
    public Mono<Capability> getCapabilityById(Long id) {
        return capabilityPersistencePort.findById(id);
    }

    @Override
    public Mono<PageModel<Capability>> getCapabilities(PageRequestModel pageRequest) {
        return capabilityPersistencePort.getCapabilities(pageRequest);
    }


    private Mono<Capability> validateData(Capability cap) {
        List<Technology> techs = cap.getTechnologies();
        List<String> errors = new ArrayList<>();

        if (cap.getName() == null || cap.getName().isBlank()) {
            errors.add(ExceptionConstans.CAPABILITY_NAME_REQUIRED);
        }
        if (cap.getName() != null && cap.getName().length() > ValueConstants.MAX_LENGTH_NAME_CAPABILITY) {
            errors.add(ExceptionConstans.CAPABILITY_NAME_EXCEEDS_LIMIT);
        }
        if (cap.getDescription() == null || cap.getDescription().isBlank()) {
            errors.add(ExceptionConstans.CAPABILITY_DESCRIPTION_REQUIRED);
        }
        if (cap.getDescription() != null && cap.getDescription().length() > ValueConstants.MAX_LENGTH_DESCRIPTION_CAPABILITY) {
            errors.add(ExceptionConstans.CAPABILITY_DESCRIPTION_EXCEEDS_LIMIT);
        }



        if (techs.size() < ValueConstants.MIN_COUNT_TECHNOLOGY|| techs.size() > ValueConstants.MAX_COUNT_TECHNOLOGY) {
            errors.add(ExceptionConstans.CAPABILITY_TECHNOLOGY_NOT_COUNT_MIN_MAX);
        }

        long unique = techs.stream().map(Technology::getId).filter(Objects::nonNull).distinct().count();
        if (unique != techs.size()) {
            errors.add(ExceptionConstans.CAPABILITY_TECHNOLOGY_DUPLICATE);
        }

        if (!errors.isEmpty()) {
            return Mono.error(new InvalidCapabilityException(String.join("; ", errors)));
        }
        return  Mono.just(cap) ;
    }
    private Mono<Capability> validateTechnologies(Capability cap) {
        List<Technology> techs = cap.getTechnologies();



        Flux<Long> ids = Flux.fromIterable(techs).map(Technology::getId);
        return technologyClientPort.getTechnologiesByIds(ids)
                .collectList()
                .flatMap(found -> {
                    if (found.size() != techs.size()) {
                        return Mono.error(new TechnologyNotExitException(ExceptionConstans.CAPABILITY_TECHNOLOGY_NOT_FOUND));
                    }
                    cap.setTechnologies(found);
                    return Mono.just(cap);
                });
    }



}
