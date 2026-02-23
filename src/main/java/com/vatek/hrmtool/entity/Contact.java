package com.vatek.hrmtool.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.entity.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends CommonEntity {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column
    private String otherContact;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String role;

    @Column(columnDefinition = "TEXT")
    private String request;

    @Column
    private Boolean allowReceiveOffers = false;

    @Column
    private Boolean agreePrivacyPolicy = false;

    @Column
    private String servicesFocusing;

    @Column
    private String ideaName;

    @Column
    private String type;

    @Column
    private String message;

    @JsonFormat(pattern = DateConstant.yyyy_MM_dd_HH_mm_ss)
    @Column
    private LocalDateTime date = LocalDateTime.now();
}
