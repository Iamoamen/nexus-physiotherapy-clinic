package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String patientName;
    @Column(nullable = false) private String phone;
    private String email;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "service_id")
    private Service service;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "therapist_id")
    private Therapist therapist;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "branch_id")
    private Branch branch;
    private LocalDate preferredDate;
    private String preferredTime;
    @Column(columnDefinition = "TEXT") private String message;
    @Enumerated(EnumType.STRING) @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Builder.Default
    private AppointmentStatus status = AppointmentStatus.PENDING;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime updatedAt;
}
