//package com.vatek.hrmtool.enumeration;
//
//import org.springframework.security.core.GrantedAuthority;
//
//public enum Role implements GrantedAuthority {
//
//    ADMIN(Code.ADMIN),
//    PROJECT_MANAGER(Code.PROJECT_MANAGER),
//    DEV_MEMBER(Code.DEV_MEMBER),
//    HR_MEMBER(Code.HR_MEMBER),
//    MARKETING_MEMBER(Code.MARKETING_MEMBER);
//
//    private final String authority;
//
//    Role(String authority) {
//        this.authority = authority;
//    }
//
//    @Override
//    public String getAuthority() {
//        return authority;
//    }
//
//    public static class Code {
//        public static final String ADMIN = "ROLE_ADMIN";
//        public static final String PROJECT_MANAGER = "ROLE_PM";
//        public static final String DEV_MEMBER = "ROLE_DEV";
//        public static final String HR_MEMBER = "ROLE_HR";
//        public static final String MARKETING_MEMBER = "ROLE_MARKETING";
//    }
//}