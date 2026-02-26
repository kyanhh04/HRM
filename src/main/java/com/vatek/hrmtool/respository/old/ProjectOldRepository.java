package com.vatek.hrmtool.respository.old;

import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.entity.UserOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectOldRepository extends JpaRepository<ProjectOld, String>, JpaSpecificationExecutor<ProjectOld> {
    ProjectOld findByProjectName(String projectName);
    
    boolean existsByMembers(UserOld user);

    @Query("SELECT p FROM ProjectOld p WHERE p.isDeleted = false AND LOWER(p.projectName) LIKE LOWER(CONCAT('%', :projectName, '%')) ORDER BY p.projectName ASC")
    Page<ProjectOld> findByProjectNameContainingAndIsDeletedFalse(@Param("projectName") String projectName, Pageable pageable);

    @Query("SELECT p FROM ProjectOld p WHERE p.isDeleted = false ORDER BY p.projectName ASC")
    Page<ProjectOld> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT p FROM ProjectOld p WHERE p.isDeleted = false AND (p.projectManager.id = :userId OR :userId IN (SELECT m.id FROM p.members m))")
    List<ProjectOld> findByUserAsManagerOrMember(@Param("userId") String userId);

    @Query("SELECT p FROM ProjectOld p WHERE p.isDeleted = false AND :userId IN (SELECT m.id FROM p.members m)")
    List<ProjectOld> findByUserAsMember(@Param("userId") String userId);

    Optional<ProjectOld> findByIdAndIsDeletedFalse(String id);

    long countByIsDeletedFalse();
}
