package com.cmalegrete.dto.request.model.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class RequestEmail {

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
}