package com.vatek.hrmtool.enumeration;

public enum Level {
    INTERN("INTERN"),
    FRESHER("FRESHER"),
    JUNIOR("JUNIOR"),
    MIDDLE("MIDDLE"),
    SENIOR("SENIOR");
    private final String level;
    Level(String level){
        this.level = level;
    }
    public String getValue(){
        return level;
    }
}
