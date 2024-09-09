package com.cmalegrete.model.assistant;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantRepository extends JpaRepository<AssistantEntity, UUID> {
    
}