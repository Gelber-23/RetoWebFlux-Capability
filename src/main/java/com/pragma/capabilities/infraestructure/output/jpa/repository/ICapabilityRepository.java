package com.pragma.capabilities.infraestructure.output.jpa.repository;

import com.pragma.capabilities.domain.model.CapabilityWithCount;
import com.pragma.capabilities.infraestructure.output.jpa.entity.CapabilityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapabilityRepository extends ReactiveCrudRepository<CapabilityEntity,Long> {

    //NAME ASC
    @Query("""
  SELECT c.id, c.name, c.description,
         COUNT(ct.technology_id) AS tech_count
    FROM capabilities c
    LEFT JOIN capability_technology ct
      ON c.id = ct.capability_id
   GROUP BY c.id, c.name, c.description
   ORDER BY c.name ASC
   LIMIT :limit OFFSET :offset
""")
    Flux<CapabilityWithCount> findPageOrderByNameAsc(@Param("limit") int limit, @Param("offset") long offset);

    // NAME DESC
    @Query("""
  SELECT c.id, c.name, c.description,
         COUNT(ct.technology_id) AS tech_count
    FROM capabilities c
    LEFT JOIN capability_technology ct
      ON c.id = ct.capability_id
   GROUP BY c.id, c.name, c.description
   ORDER BY c.name DESC
   LIMIT :limit OFFSET :offset
""")
    Flux<CapabilityWithCount> findPageOrderByNameDesc(@Param("limit") int limit, @Param("offset") long offset);

    // TECH ASC
    @Query("""
  SELECT c.id, c.name, c.description,
         COUNT(ct.technology_id) AS tech_count
    FROM capabilities c
    LEFT JOIN capability_technology ct
      ON c.id = ct.capability_id
   GROUP BY c.id, c.name, c.description
   ORDER BY tech_count ASC
   LIMIT :limit OFFSET :offset
""")
    Flux<CapabilityWithCount> findPageOrderByCountAsc(@Param("limit") int limit, @Param("offset") long offset);

    // TECH DESC
    @Query("""
  SELECT c.id, c.name, c.description,
         COUNT(ct.technology_id) AS tech_count
    FROM capabilities c
    LEFT JOIN capability_technology ct
      ON c.id = ct.capability_id
   GROUP BY c.id, c.name, c.description
   ORDER BY tech_count DESC
   LIMIT :limit OFFSET :offset
""")
    Flux<CapabilityWithCount> findPageOrderByCountDesc(@Param("limit") int limit, @Param("offset") long offset);

    @Query("SELECT COUNT(*) FROM capabilities")
    Mono<Long> countAll();
}
