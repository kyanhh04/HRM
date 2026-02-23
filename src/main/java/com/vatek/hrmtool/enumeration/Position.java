package com.vatek.hrmtool.enumeration;


public enum Position {
    FE(Code.FE),
    BE(Code.BE),
    TESTER(Code.TESTER),
    PM(Code.PM),
    HR(Code.HR),
    ADMIN(Code.ADMIN);

    private final String position;

    Position(String position){
        this.position = position;
    }

    public String getValue() {
        return position;
    }

    public static class Code {
        public static final String FE = "POSITION_FE";
        public static final String BE = "POSITION_BE";
        public static final String TESTER = "POSITION_TESTER";
        public static final String PM = "POSITION_PM";
        public static final String HR = "POSITION_HR";
        public static final String ADMIN = "POSITION_ADMIN";
    }
}
