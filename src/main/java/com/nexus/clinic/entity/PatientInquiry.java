package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_inquiries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PatientInquiry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    @Column(columnDefinition = "TEXT") private String message;
    @Builder.Default
    private boolean replied = false;
    @CreationTimestamp private LocalDateTime createdAt;
}
