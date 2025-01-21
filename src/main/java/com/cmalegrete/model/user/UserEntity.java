package com.cmalegrete.model.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cmalegrete.exception.generic.MembershipException;
import com.cmalegrete.model.dependant.DependantEntity;
import com.cmalegrete.service.util.UtilService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String tempPassword;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String militaryOrganization;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column(nullable = false, unique = true)
    private String registrationId;

    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;

    @Column(nullable = true)
    private LocalDateTime createdDateTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DependantEntity> dependants = new ArrayList<>();

    protected UserEntity(UUID id, String nome, String cpf, String email, String militaryOrganization, UserRoleEnum role, UserStatusEnum status, String registrationId) {

        if (nome == null || nome.isBlank()) {
            throw new MembershipException("Nome não pode ser nulo");
        }

        if (cpf == null || cpf.isBlank()) {
            throw new MembershipException("CPF não pode ser nulo");
        }

        if (email == null || email.isBlank()) {
            throw new MembershipException("Email não pode ser nulo");
        }

        if (role == null) {
            throw new MembershipException("Role não pode ser nula");
        }

        if (status == null) {
            throw new MembershipException("Status não pode ser nula");
        }

        if (registrationId == null) {
            throw new MembershipException("Matrícula não pode ser nula");
        }

        this.id = id;
        this.cpf = cpf;
        this.email = email;
        this.name = UtilService.toCapitalize(nome);
        this.tempPassword = UtilService.generatePassword();
        this.password = new BCryptPasswordEncoder().encode(this.tempPassword);
        this.militaryOrganization = militaryOrganization;
        this.role = role;
        this.status = status;
        this.registrationId = registrationId;
        this.createdDateTime = LocalDateTime.now();
    }

    public void expireTempPassword() {
        this.tempPassword = null;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setPasswordResetToken(String token, long expiryHours) {
        this.passwordResetToken = token;
        this.passwordResetTokenExpiry = LocalDateTime.now().plusHours(expiryHours);
    }

    public boolean isPasswordResetTokenValid(String token) {
        return this.passwordResetToken != null &&
               this.passwordResetToken.equals(token) &&
               this.passwordResetTokenExpiry != null &&
               this.passwordResetTokenExpiry.isAfter(LocalDateTime.now());
    }

    public void clearPasswordResetToken() {
        this.passwordResetToken = null;
        this.passwordResetTokenExpiry = null;
    }
}
