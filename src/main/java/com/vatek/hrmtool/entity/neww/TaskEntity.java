//package com.vatek.hrmtool.entity.neww;
//
//import com.vatek.hrmtool.entity.common.CommonEntity;
//import com.vatek.hrmtool.enumeration.TimesheetStatus;
//import com.vatek.hrmtool.enumeration.WorkingType;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "tasks")
//@Getter
//@Setter
//public class TaskEntity extends CommonEntity {
//    @Column
//    private LocalDate date;
//    @Enumerated(EnumType.STRING)
//    @Column
//    private WorkingType workingType;
//    @Column
//    private double workingHour;
//    @Column
//    private String description;
//    @Enumerated(EnumType.STRING)
//    @Column
//    private TimesheetStatus timesheetStatus;
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    private ProjectEntity project;
//}
