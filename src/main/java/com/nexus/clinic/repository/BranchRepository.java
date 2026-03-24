package com.nexus.clinic.repository;

import com.nexus.clinic.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByActiveTrueOrderByMainBranchDesc();
    Optional<Branch> findByMainBranchTrue();
}
