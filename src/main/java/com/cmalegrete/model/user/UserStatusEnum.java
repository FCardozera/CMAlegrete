package com.cmalegrete.model.user;

public enum UserStatusEnum {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    CONTRACT_PENDING("CONTRACT_PENDING"),
    APPROVAL_PENDING("APPROVAL_PENDING"),
    PAYMENT_PENDING("PAYMENT_PENDING"),
    PAYMENT_LATE("PAYMENT_LATE"),
    REJECTED("REJECTED"),
    TEMPORARY("TEMPORARY");

    private String status;

    UserStatusEnum (String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
    
}
