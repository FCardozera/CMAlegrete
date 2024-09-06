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
public class MemberRegisterRequest extends RequestEmail {
    
    private String name;
    private String cpf;
    private String rg;
    private String phoneNumber;
    private String address;
    private String militaryOrganization;


}