package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conditions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Condition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameEn;
    private String nameAr;
    @Column(columnDefinition = "TEXT") private String descriptionEn;
    @Column(columnDefinition = "TEXT") private String descriptionAr;
    private String bodyArea;
    private String iconClass;
    @Column(unique = true) private String slug;
    @Builder.Default
    private boolean active = true;
}
