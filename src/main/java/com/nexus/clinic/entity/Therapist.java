package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "therapists")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Therapist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameEn;
    private String nameAr;
    private String titleEn;
    private String titleAr;
    private String specialization;
    private String photoUrl;
    @Column(columnDefinition = "TEXT") private String bioEn;
    @Column(columnDefinition = "TEXT") private String bioAr;
    private String instagramUrl;
    private String linkedinUrl;
    private Integer yearsExperience;
    @Builder.Default
    private boolean active = true;
    private Integer sortOrder;
}
