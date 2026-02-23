package com.vatek.hrmtool.enumeration;

public enum ConfigKey {
    LEVEL("LEVEL"),
    POSITION("POSITION"),
    STATUS("STATUS"),
    WORKINGTYPE("WORKINGTYPE");
    private final String configKey;
    ConfigKey(String configKey){
        this.configKey = configKey;
    }
    public String getValue(){
        return configKey;
    }
}
