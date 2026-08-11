package com.elms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

// Holiday entity storing company public holidays
@Entity
@Table(name = "holidays")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique holiday date
    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;
}
