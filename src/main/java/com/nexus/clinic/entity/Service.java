package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "services")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Service {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nameEn;
    @Column(nullable = false) private String nameAr;
    @Column(columnDefinition = "TEXT") private String descriptionEn;
    @Column(columnDefinition = "TEXT") private String descriptionAr;
    private String iconClass;
    private String imageUrl;
    @Column(unique = true) private String slug;
    private Integer sortOrder;
    @Builder.Default
    private boolean active = true;
    @Column(precision = 10, scale = 2) private BigDecimal priceFrom;
}
