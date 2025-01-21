package com.cmalegrete.model.dependant;

import java.util.UUID;

import com.cmalegrete.dto.request.model.dependant.DependantRegisterRequest;
import com.cmalegrete.dto.request.model.dependant.DependantUpdateRequest;
import com.cmalegrete.exception.generic.UpdateException;
import com.cmalegrete.model.user.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "dependant")
@Table(name = "tb_dependant")
@ToString
@NoArgsConstructor
@Getter
@Setter
public class DependantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public DependantEntity(DependantRegisterRequest request) {
        this.cpf = request.getCpf();
        this.name = request.getName();
        this.email = request.getEmail();
    }

    public void update(DependantUpdateRequest request) {
        if (request == null) {
            throw new UpdateException("Requisição de atualização do dependente não pode ser nula");
        }

        this.setName(validateAndAssign(this.getName(), request.getName(), "Nome do dependente não pode ser nulo"));
        this.setEmail(validateAndAssign(this.getEmail(), request.getEmail(), "Email do dependente não pode ser nulo"));
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
