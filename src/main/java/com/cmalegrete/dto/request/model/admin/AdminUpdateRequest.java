package com.cmalegrete.dto.request.model.admin;

import com.cmalegrete.dto.request.model.util.RequestEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminUpdateRequest extends RequestEmail {
    
    private String name;
    private String cpf;
    private String password;

}