package com.nexus.clinic.repository;

import com.nexus.clinic.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findByBranchIdOrderByDayOfWeekAsc(Long branchId);
}
