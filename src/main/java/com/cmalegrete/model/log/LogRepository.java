package com.cmalegrete.model.log;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity, UUID> {
    
}
