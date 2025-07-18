package com.pragma.capabilities.application.mapper.request;

import com.pragma.capabilities.application.dto.request.PageRequest;
import com.pragma.capabilities.domain.model.page.PageRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPageRequestMapper {
    PageRequestModel toModel (PageRequest pageRequest);
    PageRequest toRequest (PageRequestModel pageRequestModel);
}
