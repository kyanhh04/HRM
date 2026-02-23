//package com.vatek.hrmtool.respository;
//
//import com.vatek.hrmtool.entity.neww.LeaveRequestEntity;
//import com.vatek.hrmtool.enumeration.RequestStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//public interface RequestLeaveRepository extends JpaRepository<LeaveRequestEntity, Long>, JpaSpecificationExecutor<LeaveRequestEntity> {
//    @Query("select (count(l) > 0) from LeaveRequestEntity l where l.user.id = :userId and (l.startDate <= :endDate and l.endDate >= :startDate)")
//    boolean existsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
//    @Query("select (count(l) > 0) from LeaveRequestEntity l where l.user.id = :userId and :date = l.startDate and (:startTime < l.endTime and :endTime > l.startTime)")
//    boolean existsByUserIdAndTimeRange(Long userId, LocalDate date, LocalTime startTime, LocalTime endTime);
//    @Query("select (count(l) > 0) from LeaveRequestEntity l where l.user.id = :userId and :date between l.startDate and l.endDate and l.leaveType = com.vatek.hrmtool.enumeration.LeaveType.MULTI")
//    boolean existsByUserIdAndDateRangeDaily(Long userId, LocalDate date);
//    @Query("select lr from LeaveRequestEntity lr where lr.startDate <= :date and :date <= lr.endDate")
//    List<LeaveRequestEntity> findByDate(LocalDate date);
//    @Query("select lr from LeaveRequestEntity lr where lr.user.id = :userId")
//    List<LeaveRequestEntity> findByUserId(Long userId);
//    @Query("select lr from LeaveRequestEntity lr where lr.status = :status")
//    Page<LeaveRequestEntity> findByStatus(RequestStatus status, Pageable pageable);
//    @Query("SELECT DISTINCT lr FROM LeaveRequestEntity lr " +
//           "JOIN FETCH lr.user u " +
//           "JOIN u.roles r " +
//           "WHERE lr.status = :status AND (r.role = 'DEV_MEMBER' OR r.role = 'HR_MEMBER' OR r.role = 'MARKETING_MEMBER')")
//    Page<LeaveRequestEntity> findByStatusAndEmployeeRoles(RequestStatus status, Pageable pageable);
//}
