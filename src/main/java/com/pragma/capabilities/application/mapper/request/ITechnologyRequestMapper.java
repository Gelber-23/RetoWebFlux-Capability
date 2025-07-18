package com.pragma.capabilities.application.mapper.request;

import com.pragma.capabilities.application.dto.request.TechnologyRequest;
import com.pragma.capabilities.domain.model.web.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyRequestMapper {
    TechnologyRequest toRequest (Technology technology);
    Technology toModel (TechnologyRequest technology);
}