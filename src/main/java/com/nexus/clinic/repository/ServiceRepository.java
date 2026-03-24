package com.nexus.clinic.repository;

import com.nexus.clinic.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByActiveTrueOrderBySortOrderAsc();
    Optional<Service> findBySlug(String slug);
}
