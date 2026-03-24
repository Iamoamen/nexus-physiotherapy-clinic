package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blog_posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BlogPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleEn;
    private String titleAr;
    @Column(unique = true) private String slug;
    @Column(columnDefinition = "TEXT") private String summaryEn;
    @Column(columnDefinition = "TEXT") private String summaryAr;
    @Column(columnDefinition = "LONGTEXT") private String contentEn;
    @Column(columnDefinition = "LONGTEXT") private String contentAr;
    private String coverImageUrl;
    private String authorName;
    private String category;
    @Enumerated(EnumType.STRING) @Column(columnDefinition = "VARCHAR(20)") private PostStatus status = PostStatus.DRAFT;
    private LocalDate publishedDate;
    @CreationTimestamp private LocalDateTime createdAt;
}
