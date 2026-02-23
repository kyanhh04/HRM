package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects_old")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectOld extends CommonEntity {

    @Column
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id")
    private UserOld projectManager;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserOld> members;

    @Column
    private String state = "INCOMING";

    @Column
    private LocalDateTime startDate;

    @Column
    private Boolean isDeleted = false;

    @Column
    private LocalDate endDate;

    @Column
    private String clientName;
}
