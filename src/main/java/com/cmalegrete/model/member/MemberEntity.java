package com.cmalegrete.model.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cmalegrete.dto.request.model.member.MemberRegisterRequest;
import com.cmalegrete.dto.request.model.member.MemberUpdateRequest;
import com.cmalegrete.exception.generic.MembershipException;
import com.cmalegrete.exception.generic.UpdateException;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.model.user.UserRoleEnum;
import com.cmalegrete.model.user.UserStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "member")
@Table(name = "tb_member")
@ToString
@Getter
@Setter
@NoArgsConstructor
public class MemberEntity extends UserEntity {

    @Column(nullable = true)
    private String militaryOrganization;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String address;

    public MemberEntity(MemberRegisterRequest request) {
        super(null, request.getName(), request.getCpf(), request.getEmail(), UserRoleEnum.MEMBER, UserStatusEnum.PENDING);

        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new MembershipException("Telefone não pode ser nulo");
        }

        this.phoneNumber = request.getPhoneNumber();
        this.militaryOrganization = request.getMilitaryOrganization();
        this.rg = request.getRg();
        this.address = request.getAddress();
    }

    public MemberEntity(MemberRegisterRequest request, UserStatusEnum status) {
        super(null, request.getName(), request.getCpf(), request.getEmail(), UserRoleEnum.MEMBER, status);
    }

    public void update(MemberUpdateRequest request) {
        if (request == null) {
            throw new UpdateException("Requisição de atualização do associado não pode ser nula");
        }

        this.setName(validateAndAssign(this.getName(), request.getName(), "Nome do associado não pode ser nulo"));
        this.setEmail(validateAndAssign(this.getEmail(), request.getEmail(), "Email do associado não pode ser nulo"));
        this.setPhoneNumber(validateAndAssign(this.getPhoneNumber(), request.getPhoneNumber(), "Telefone do associado não pode ser nulo"));
        this.setMilitaryOrganization(validateAndAssign(this.getMilitaryOrganization(), request.getMilitaryOrganization(), "Organização Militar do associado não pode ser nulo"));

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
