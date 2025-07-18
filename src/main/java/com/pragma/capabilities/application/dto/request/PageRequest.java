package com.pragma.capabilities.application.dto.request;

import com.pragma.capabilities.domain.model.enumdata.SortBy;
import com.pragma.capabilities.domain.model.enumdata.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageRequest {
    private int page;
    private int size;
    private SortOrder order;
    private SortBy sortBy;
}
