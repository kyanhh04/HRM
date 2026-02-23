package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_time_off")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestOld extends CommonEntity {

    @Column
    private Boolean isMorning;

    @Column
    private Boolean isAfternoon;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDate fromDay;

    @Column
    private String reason;

    @Column
    private LocalDate toDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Config status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserOld user;
}
