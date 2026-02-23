//package com.vatek.hrmtool.entity.neww;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.vatek.hrmtool.entity.common.CommonEntity;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Collection;
//import java.util.List;
//
//@Entity
//@Table(name = "users")
//@Getter
//@Setter
//public class UserEntity extends CommonEntity {
//    @Column
//    private String name;
//    @Column(unique = true, nullable = false)
//    private String email;
//    @Column(unique = true, nullable = false)
//    private String username;
//    @Column
//    private String password;
//    @Column(unique = true, nullable = false)
//    private String identityCard;
//    @Column(unique = true, nullable = false)
//    private String phoneNumber1;
//    @Column
//    private String avatarUrl;
//    @Column
//    private String currentAddress;
//    @Column
//    private String permanentAddress;
//    @Column
//    private String accessToken;
//    @Column
//    private boolean tokenStatus;
//    @Column
//    private boolean isEnabled;
//    @Column
//    private String level;
//    @Column
//    private String programLanguage;
//    @Column
//    private String position;
//    @Column
//    private LocalTime startTime;
//    @Column
//    private LocalTime endTime;
//    @Column
//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(
//                    name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "role_id", referencedColumnName = "id"))
//    private Collection<RoleEntity> roles;
//    @Column
//    private LocalDate DateOfBirth;
//    @JsonIgnore
//    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
//    private Collection<ProjectEntity> projects;
//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    private List<TaskEntity> tasks;
//    @JsonIgnore
//    @OneToMany(mappedBy = "projectManager")
//    private List<ProjectEntity> manageProjectList;
//    @JsonIgnore
//    @OneToMany(mappedBy = "user")
//    private List<LeaveRequestEntity> leaveRequests;
//}
