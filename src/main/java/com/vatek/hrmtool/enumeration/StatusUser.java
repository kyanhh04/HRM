package com.vatek.hrmtool.enumeration;

public enum StatusUser {
    INACTIVE("Inactive"),
    ACTIVE("Active"),
    DEACTIVATED("Deactivated");
    private final String statusUser;
    StatusUser(String statusUser){
        this.statusUser = statusUser;
    }
    public String getValue(){
        return statusUser;
    }
}
