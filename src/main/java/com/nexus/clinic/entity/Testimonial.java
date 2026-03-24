package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "testimonials")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Testimonial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientName;
    private String patientNameAr;
    @Column(columnDefinition = "TEXT") private String contentEn;
    @Column(columnDefinition = "TEXT") private String contentAr;
    private Integer rating;
    @Column(name = "condition_name") private String condition;
    @Builder.Default
    private boolean approved = false;
    @Builder.Default
    private boolean featured = false;
    @CreationTimestamp private LocalDateTime createdAt;
}
