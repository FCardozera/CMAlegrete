package com.cmalegrete.model.assistant;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmalegrete.model.assistant.AssistantEntity;

public interface AssistantRepository extends JpaRepository<AssistantEntity, UUID> {
    
}