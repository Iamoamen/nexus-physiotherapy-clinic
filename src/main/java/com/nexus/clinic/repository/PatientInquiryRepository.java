package com.nexus.clinic.repository;

import com.nexus.clinic.entity.PatientInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientInquiryRepository extends JpaRepository<PatientInquiry, Long> {
    Page<PatientInquiry> findByRepliedFalseOrderByCreatedAtDesc(Pageable pageable);
    Page<PatientInquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByRepliedFalse();
}
