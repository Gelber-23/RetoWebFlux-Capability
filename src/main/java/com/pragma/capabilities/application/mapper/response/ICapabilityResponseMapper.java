package com.pragma.capabilities.application.mapper.response;

import com.pragma.capabilities.application.dto.response.CapabilityResponse;
import com.pragma.capabilities.domain.model.Capability;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityResponseMapper {

    CapabilityResponse toResponse(Capability model);


}
