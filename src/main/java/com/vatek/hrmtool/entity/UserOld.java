package com.vatek.hrmtool.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.StatusUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users_old")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOld extends CommonEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullName;

    @Column
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(name = "citizen_id")
    private String citizenID;

    @Column
    private LocalDateTime dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDate dateOfBirth;

    @Column
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Config level;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_positions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private List<Config> positions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_mentor_id")
    private UserOld onboardingMentor;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDate onboardingDate;

    @Column
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Image avatar;

    @Column
    private String refreshToken;

    @Column()
    private String status = StatusUser.INACTIVE.getValue();
}
