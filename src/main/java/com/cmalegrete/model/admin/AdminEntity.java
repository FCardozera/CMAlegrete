package com.cmalegrete.model.admin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cmalegrete.dto.request.model.admin.AdminRegisterRequest;
import com.cmalegrete.dto.request.model.admin.AdminUpdateRequest;
import com.cmalegrete.exception.generic.UpdateException;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.model.user.UserRoleEnum;
import com.cmalegrete.model.user.UserStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "admin")
@Table(name = "tb_admin")
@ToString
@NoArgsConstructor
public class AdminEntity extends UserEntity {

    public AdminEntity(AdminRegisterRequest request) {
        super(null, request.getName(), request.getCpf(), request.getEmail(), UserRoleEnum.ADMIN, UserStatusEnum.ACTIVE);
    }

    public AdminEntity(AdminRegisterRequest request, UserStatusEnum status) {
        super(null, request.getName(), request.getCpf(), request.getEmail(), UserRoleEnum.ADMIN, status);
    }

    public void update(AdminUpdateRequest request) {
        if (request == null) {
            throw new UpdateException("Requisição de atualização do admin não pode ser nula");
        }

        this.setName(validateAndAssign(this.getName(), request.getName(), "Nome do admin não pode ser nulo"));
        this.setEmail(validateAndAssign(this.getEmail(), request.getEmail(), "Email do admin não pode ser nulo"));

        if (request.getPassword() != null) {
            this.setPassword(validateAndAssign(this.getPassword(), new BCryptPasswordEncoder().encode(request.getPassword()), "Senha do associado não pode ser nula"));
        }

        if (this.getTempPassword() != null) {
            super.expireTempPassword();
        }
    }

    private String validateAndAssign(String originalValue, String value, String errorMessage) {
        if (value == null) {
            return originalValue;
        }

        if (value.isBlank()) {
            throw new UpdateException(errorMessage);
        }

        return value;
    }

}
