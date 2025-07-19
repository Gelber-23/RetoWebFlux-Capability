package com.pragma.capabilities.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class CapabilityResponse {

    private Long id;
    private String name;
    private String description;
    private List<TechnologyResponse> technologies;
}
