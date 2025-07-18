package com.pragma.capabilities.infraestructure.output.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability_technology")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapabilityTechnologyEntity {
    @Id
    private Long id;
    private Long capabilityId;
    private Long technologyId;
}
