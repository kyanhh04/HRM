//package com.vatek.hrmtool.respository;
//
//import com.vatek.hrmtool.entity.neww.ProjectEntity;
//
//import jakarta.transaction.Transactional;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//
//public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, JpaSpecificationExecutor<ProjectEntity> {
//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM users_projects WHERE project_id = :projectId AND user_id = :userId", nativeQuery = true)
//    void removeMemberFromProject(@Param("projectId") Long projectId, @Param("userId") Long userId);
//    @Query(value = "select p from ProjectEntity p where lower(p.projectName) like lower(concat('%', :keyword, '%'))")
//    Page<ProjectEntity> searchByKeyword(String keyword, Pageable pageable);
//}
