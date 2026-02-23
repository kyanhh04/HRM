//package com.vatek.hrmtool.entity.neww;
//
//import com.vatek.hrmtool.entity.common.CommonEntity;
//import com.vatek.hrmtool.enumeration.LeaveType;
//import com.vatek.hrmtool.enumeration.RequestStatus;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.*;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "leave_requests")
//public class LeaveRequestEntity extends CommonEntity {
//    @Column
//    private LocalTime startTime;
//    @Column
//    private LocalTime endTime;
//    @Column
//    private LocalDate startDate;
//    @Column
//    private LocalDate endDate;
//    @Column
//    private double leaveHours;
//    @Column
//    private String reason;
//    @Column
//    @Enumerated(EnumType.STRING)
//    private RequestStatus status;
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
//    @Enumerated(EnumType.STRING)
//    @Column
//    private LeaveType leaveType;
//}
