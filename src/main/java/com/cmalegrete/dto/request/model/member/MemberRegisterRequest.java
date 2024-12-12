package com.cmalegrete.dto.request.model.member;

import org.hibernate.validator.constraints.br.CPF;

import com.cmalegrete.dto.request.model.util.RequestEmail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MemberRegisterRequest extends RequestEmail {
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @CPF(message = "CPF inválido")
    @Size(min = 14, max = 14, message = "CPF deve ter 14 caracteres")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    private String phoneNumber;

    @NotBlank(message = "Endereço é obrigatório")
    private String address;

    @NotBlank(message = "Bairro é obrigatório")
    private String militaryOrganization;


}