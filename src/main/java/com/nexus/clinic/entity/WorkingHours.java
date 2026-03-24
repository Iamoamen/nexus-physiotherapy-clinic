package com.nexus.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "working_hours")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkingHours {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING) private DayOfWeek dayOfWeek;
    private String openTime;
    private String closeTime;
    @Builder.Default
    private boolean closed = false;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "branch_id")
    private Branch branch;
}
