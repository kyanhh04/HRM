package com.vatek.hrmtool.respository.old;

import com.vatek.hrmtool.entity.TimesheetOld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimesheetOldRepository extends JpaRepository<TimesheetOld, String>, JpaSpecificationExecutor<TimesheetOld> {
}
