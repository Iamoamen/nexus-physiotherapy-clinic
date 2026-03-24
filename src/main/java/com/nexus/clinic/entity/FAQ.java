package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faqs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FAQ {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionEn;
    private String questionAr;
    @Column(columnDefinition = "TEXT") private String answerEn;
    @Column(columnDefinition = "TEXT") private String answerAr;
    private String category;
    private Integer sortOrder;
    @Builder.Default
    private boolean active = true;
}
