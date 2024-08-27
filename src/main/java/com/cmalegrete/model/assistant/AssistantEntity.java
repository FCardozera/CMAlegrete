package com.cmalegrete.model.assistant;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cmalegrete.dto.request.model.assistant.AssistantRegisterRequest;
import com.cmalegrete.dto.request.model.assistant.AssistantUpdateRequest;
import com.cmalegrete.exception.generic.UpdateException;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.model.user.UserRoleEnum;
import com.cmalegrete.model.user.UserStatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "assistant")
@Table(name = "tb_assistant")
@ToString
@NoArgsConstructor
public class AssistantEntity extends UserEntity {

    public AssistantEntity(AssistantRegisterRequest request) {
        super(null, request.getName(), request.getCpf(), request.getEmail(), UserRoleEnum.ASSISTANT, UserStatusEnum.ACTIVE);
    }

    public AssistantEntity(AssistantRegisterRequest request, UserStatusEnum status) {
        super(null, request.getName(), request.getCpf(), request.getEmail(), UserRoleEnum.ASSISTANT, status);
    }

    public void update(AssistantUpdateRequest request) {
        if (request == null) {
            throw new UpdateException("Requisição de atualização do assistant não pode ser nula");
        }

        this.setName(validateAndAssign(this.getName(), request.getName(), "Nome do assistant não pode ser nulo"));
        this.setEmail(validateAndAssign(this.getEmail(), request.getEmail(), "Email do assistant não pode ser nulo"));

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
