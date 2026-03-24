package com.nexus.clinic.repository;

import com.nexus.clinic.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByCode(String code);
}
