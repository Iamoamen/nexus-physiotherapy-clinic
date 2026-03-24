package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "branches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Branch {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameEn;
    private String nameAr;
    private String addressEn;
    private String addressAr;
    private String phone;
    private String whatsapp;
    private String googleMapsEmbed;
    private String latitude;
    private String longitude;
    @Builder.Default
    private boolean active = true;
    @Builder.Default
    private boolean mainBranch = false;
}
