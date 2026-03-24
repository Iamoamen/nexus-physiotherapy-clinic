package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_templates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmailTemplate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Machine key — never changes. e.g. "booking_confirmation" */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 150) private String nameEn;
    @Column(nullable = false, length = 150) private String nameAr;

    @Column(nullable = false, length = 250) private String subjectEn;
    @Column(nullable = false, length = 250) private String subjectAr;

    @Column(nullable = false, columnDefinition = "TEXT") private String bodyEn;
    @Column(nullable = false, columnDefinition = "TEXT") private String bodyAr;

    @Builder.Default
    private boolean active = true;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
