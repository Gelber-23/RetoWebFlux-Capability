package com.pragma.capabilities.infraestructure.output.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;



@Table("capabilities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapabilityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}
