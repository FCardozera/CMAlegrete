package com.cmalegrete.dto.request.model.assistant;


import com.cmalegrete.dto.request.model.util.RequestEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AssistantRegisterRequest extends RequestEmail {
    
    private String name;
    private String cpf;

}