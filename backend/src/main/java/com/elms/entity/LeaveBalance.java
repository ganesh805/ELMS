package com.elms.entity;

import jakarta.persistence.*;
import lombok.*;

// LeaveBalance entity tracking leave quotas, usage, and remaining balance per user per year
@Entity
@Table(
    name = "leave_balances",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_leavetype_year", columnNames = {"user_id", "leave_type_id", "year"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User owning this leave balance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Leave type category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer allocated;

    @Column(nullable = false)
    private Integer used;

    @Column(nullable = false)
    private Integer remaining;
}
