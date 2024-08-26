package com.cmalegrete.model.user;

public enum UserStatusEnum {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PENDING("PENDING"),
    REJECTED("REJECTED");

    private String role;

    UserStatusEnum (String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
    
}
