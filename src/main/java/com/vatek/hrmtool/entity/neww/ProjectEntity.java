//package com.vatek.hrmtool.entity.neww;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.vatek.hrmtool.entity.common.CommonEntity;
//import com.vatek.hrmtool.enumeration.ProjectState;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.List;
//
//@Entity
//@Table(name = "projects")
//@Getter
//@Setter
//public class ProjectEntity extends CommonEntity {
//    @Column(nullable = false)
//    private String clientName;
//    @Column(unique = true, nullable = false)
//    private String projectName;
//    @ManyToOne
//    @JoinColumn(name = "pm_id", referencedColumnName = "id")
//    private UserEntity projectManager;
//    @Column
//    private LocalDate startDate;
//    @Column
//    private LocalDate endDate;
//    @Enumerated(EnumType.STRING)
//    @Column
//    private ProjectState projectState;
//    @JsonIgnore
//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "users_projects",
//            joinColumns = @JoinColumn(
//                    name = "project_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "user_id", referencedColumnName = "id")
//    )
//    private Collection<UserEntity> users;
//    @JsonIgnore
//    @OneToMany(mappedBy = "project")
//    List<TaskEntity> tasks;
//
//}
