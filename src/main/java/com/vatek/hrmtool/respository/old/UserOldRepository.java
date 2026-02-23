package com.vatek.hrmtool.respository.old;

import com.vatek.hrmtool.entity.UserOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserOldRepository extends JpaRepository<UserOld, String>, JpaSpecificationExecutor<UserOld> {
    Optional<UserOld> findByEmail(String email);
    Optional<UserOld> findByUsername(String username);
    UserOld findByUsernameOrEmail(String username, String email);
    Optional<UserOld> findByEmailAndIsDeletedFalse(String email);
    Optional<UserOld> findByUsernameAndIsDeletedFalse(String username);
    Optional<UserOld> findByIdAndIsDeletedFalse(String id);
    long countByIsDeletedFalse();
    @Query("SELECT DISTINCT u FROM UserOld u " +
           "LEFT JOIN u.level l " +
           "LEFT JOIN u.positions p " +
           "WHERE u.isDeleted = false " +
           "AND (:level IS NULL OR l.value = :level) " +
           "AND (:name IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "ORDER BY u.name ASC")
    Page<UserOld> findAllWithFilters(
        @Param("level") String level,
        @Param("name") String name,
        Pageable pageable);

    @Query("SELECT DISTINCT u FROM UserOld u " +
           "LEFT JOIN u.level l " +
           "LEFT JOIN u.positions p " +
           "WHERE u.isDeleted = false " +
           "AND (:level IS NULL OR l.value = :level) " +
           "AND (CAST(:positions AS string) IS NULL OR p.value IN :positions) " +
           "AND (:name IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "ORDER BY u.name ASC")
    Page<UserOld> findAllWithFiltersAndPositions(
        @Param("level") String level,
        @Param("positions") List<String> positions,
        @Param("name") String name,
        Pageable pageable);

    @Query("SELECT DISTINCT u FROM UserOld u " +
           "LEFT JOIN u.positions p " +
           "WHERE u.isDeleted = false " +
           "AND p.id IN :positionIds " +
           "ORDER BY u.name ASC")
    Page<UserOld> findByPositionsAndIsDeletedFalse(
        @Param("positionIds") List<String> positionIds,
        Pageable pageable);

    @Query("SELECT DISTINCT u FROM UserOld u " +
           "LEFT JOIN u.positions p " +
           "WHERE u.isDeleted = false " +
           "AND p.id IN :positionIds " +
           "AND (LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
           "ORDER BY u.name ASC")
    Page<UserOld> findByNameOrUsernameAndPositionsAndIsDeletedFalse(
        @Param("name") String name,
        @Param("username") String username,
        @Param("positionIds") List<String> positionIds,
        Pageable pageable);
}
