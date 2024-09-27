package com.cmalegrete.model.sendcontracttoken;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SendContractTokenRepository extends JpaRepository<SendContractTokenEntity, UUID> {

    Optional<SendContractTokenEntity> findByToken(String token);
    Optional<SendContractTokenEntity> findByUserId(UUID userId);
    
}