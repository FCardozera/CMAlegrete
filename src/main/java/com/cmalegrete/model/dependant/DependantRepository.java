package com.cmalegrete.model.dependant;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DependantRepository extends JpaRepository<DependantEntity, UUID> {
    
}