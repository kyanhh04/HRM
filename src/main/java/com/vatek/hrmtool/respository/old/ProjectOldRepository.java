package com.vatek.hrmtool.respository.old;

import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.entity.UserOld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectOldRepository extends JpaRepository<ProjectOld, String>, JpaSpecificationExecutor<ProjectOld> {
    ProjectOld findByProjectName(String projectName);
    
    boolean existsByMembers(UserOld user);
}
