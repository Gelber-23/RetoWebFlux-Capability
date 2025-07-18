package com.pragma.capabilities.infraestructure.output.jpa.mapper;

import com.pragma.capabilities.domain.model.Capability;
import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityEntityMapper {
    Capability toModel (CapabilityEntity technologyEntity);
    CapabilityEntity toEntity ( Capability technology);

}
