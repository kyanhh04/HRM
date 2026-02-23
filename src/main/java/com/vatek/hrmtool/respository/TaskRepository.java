//package com.vatek.hrmtool.respository;
//
//import com.vatek.hrmtool.entity.neww.TaskEntity;
//import com.vatek.hrmtool.enumeration.TimesheetStatus;
//import com.vatek.hrmtool.enumeration.WorkingType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
//    List<TaskEntity> findByUserIdAndDate(Long id, LocalDate date);
//    @Query("select t from TaskEntity t where t.user.id = :id and t.date between :start and :end")
//    List<TaskEntity> findByUserIdAndDateBetween(Long id, LocalDate start, LocalDate end);
//    List<TaskEntity> findByUserIdAndProjectIdAndDateAndTimesheetStatusAndWorkingType(Long id, Long projectId, LocalDate date, TimesheetStatus timesheetStatus, WorkingType workingType);
//    List<TaskEntity> findByUserIdAndProjectIdAndDateAndTimesheetStatus(Long id, Long projectId, LocalDate date, TimesheetStatus timesheetStatus);
//
//}
