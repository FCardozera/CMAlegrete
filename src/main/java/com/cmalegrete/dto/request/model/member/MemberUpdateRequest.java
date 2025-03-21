package com.cmalegrete.dto.request.model.member;

import com.cmalegrete.dto.request.model.util.RequestEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MemberUpdateRequest extends RequestEmail {
    
    private String name;
    private String cpf;
    private String password;
    private String militaryOrganization;
    private String phoneNumber;

}