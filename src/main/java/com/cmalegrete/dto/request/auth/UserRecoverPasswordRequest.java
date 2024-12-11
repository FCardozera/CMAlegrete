package com.cmalegrete.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecoverPasswordRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email
    private String email;
}
