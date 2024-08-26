package com.cmalegrete.model.user;

public enum UserRoleEnum {

    ADMIN("ADMIN"),
    ASSISTANT("ASSISTANT"),
    MEMBER("MEMBER");

    private String role;

    UserRoleEnum (String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
    
}
