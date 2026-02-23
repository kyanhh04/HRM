package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auths")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth extends CommonEntity {

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String googleID;

    @Column
    private String facebookID;

    @Column
    private String appleID;

    @Column
    private String passwordHash;

    @Column
    private LocalDateTime lastSignedInAt;

    @Column
    private LocalDateTime createAt;

    @Column
    private Boolean disabled = false;

    @Column
    private Boolean emailVerified = false;

    @Column
    private String salt;
}
