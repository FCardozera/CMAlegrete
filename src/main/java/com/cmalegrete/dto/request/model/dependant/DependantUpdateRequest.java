package com.cmalegrete.dto.request.model.dependant;

import com.cmalegrete.dto.request.model.util.RequestEmail;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DependantUpdateRequest extends RequestEmail {
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;
}