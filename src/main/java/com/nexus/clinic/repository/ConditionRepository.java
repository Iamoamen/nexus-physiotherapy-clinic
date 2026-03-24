package com.nexus.clinic.repository;

import com.nexus.clinic.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
    List<Condition> findByActiveTrueOrderByBodyAreaAsc();
    List<Condition> findByBodyAreaAndActiveTrue(String bodyArea);
    Optional<Condition> findBySlug(String slug);
}
