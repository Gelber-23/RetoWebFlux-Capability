package com.pragma.capabilities.application.mapper.response;

import com.pragma.capabilities.application.dto.response.TechnologyResponse;
import com.pragma.capabilities.domain.model.web.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyResponseMapper {
    TechnologyResponse toResponse (Technology technology);
}
