package com.vatek.hrmtool.enumeration;

public enum WorkingType {
//    ALL("All"),
    NORMAL("Normal working hours"),
    OT("Overtime(OT)"),
    OT_CLIENT("Bonus");
    private final String type;
    WorkingType(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
