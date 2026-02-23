package com.vatek.hrmtool.dto;

import lombok.Data;

@Data
public class ContactDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String otherContact;
    private String country;
    private String companyName;
    private String role;
    private String request;
    private Boolean allowReceiveOffers;
    private Boolean agreePrivacyPolicy;
    private String ideaName;
    private String type;
    private String message;
    private String servicesFocusing;
}
