package com.nexus.clinic.repository;

import com.nexus.clinic.entity.Testimonial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    List<Testimonial> findByApprovedTrueAndFeaturedTrueOrderByCreatedAtDesc();
    List<Testimonial> findByApprovedTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Testimonial> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
