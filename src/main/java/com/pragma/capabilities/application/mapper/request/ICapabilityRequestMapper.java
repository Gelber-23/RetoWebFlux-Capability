package com.pragma.capabilities.application.mapper.request;
import com.pragma.capabilities.application.dto.request.CapabilityRequest;
import com.pragma.capabilities.domain.model.Capability;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityRequestMapper {

    Capability toModel(CapabilityRequest request);
}
