package com.nexus.clinic.repository;

import com.nexus.clinic.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByActiveTrueOrderBySortOrderAsc();
    List<FAQ> findByCategoryAndActiveTrueOrderBySortOrderAsc(String category);
}
