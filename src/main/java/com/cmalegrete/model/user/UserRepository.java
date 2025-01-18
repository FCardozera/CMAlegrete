package com.cmalegrete.model.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByEmail(String email);
    UserEntity findByCpf(String cpf);
    UserEntity findByRegistrationId(String registrationid);
    UserEntity findByPasswordResetToken(String password_reset_token);

    @Query("SELECT u.role FROM UserEntity u WHERE u.email = :email")
    UserRoleEnum findRoleByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u WHERE u.role = 'ADMIN'")
    List<UserEntity> findByRoleAdmin();

    @Query("SELECT u FROM UserEntity u WHERE u.role = 'ASSISTANT'")
    List<UserEntity> findByRoleAssistant();

    @Query("SELECT u FROM UserEntity u WHERE u.role = 'MEMBER'")
    List<UserEntity> findByRoleMember();
}