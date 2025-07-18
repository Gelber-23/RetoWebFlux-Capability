package com.pragma.capabilities.application.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CapabilityRequest {
    private String name;
    private String description;
    private List<TechnologyRequest> technologies;
}
