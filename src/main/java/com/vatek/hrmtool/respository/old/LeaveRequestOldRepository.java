package com.vatek.hrmtool.respository.old;

import com.vatek.hrmtool.entity.LeaveRequestOld;
import com.vatek.hrmtool.enumeration.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestOldRepository extends JpaRepository<LeaveRequestOld, String>, JpaSpecificationExecutor<LeaveRequestOld> {
    @Query("SELECT lr FROM LeaveRequestOld lr WHERE " +
           "((lr.fromDay <= :toDay AND lr.toDay >= :fromDay) AND " +
           "((lr.isMorning = true AND :isMorning = true) OR (lr.isAfternoon = true AND :isAfternoon = true)))")
    List<LeaveRequestOld> findOverlappingRequests(@Param("fromDay") LocalDate fromDay, @Param("toDay") LocalDate toDay,
                                                  @Param("isMorning") boolean isMorning, @Param("isAfternoon") boolean isAfternoon);
    List<LeaveRequestOld> findByStatusNot(RequestStatus status);
    @Query("SELECT lr FROM LeaveRequestOld lr WHERE lr.user.id = :userId AND lr.status.value != 'REJECTED'")
    List<LeaveRequestOld> findByUserIdAndStatusNot(@Param("userId") String userId);
    
    @Query("SELECT lr FROM LeaveRequestOld lr WHERE lr.user.id IN :userIds AND lr.status.id = :statusId AND lr.fromDay <= :toDay AND lr.toDay >= :fromDay")
    List<LeaveRequestOld> findByUserIdsAndStatusAndDateRangeWithDetails(@Param("userIds") List<String> userIds, @Param("statusId") String statusId, @Param("fromDay") LocalDate fromDay, @Param("toDay") LocalDate toDay);
}
