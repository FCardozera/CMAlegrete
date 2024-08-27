package com.cmalegrete.dto.request.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ContactMessageRequest extends RequestEmail {
    
    private String name;
    private String subject;
    private String message;

}