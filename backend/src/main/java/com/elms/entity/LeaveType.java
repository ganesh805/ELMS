package com.elms.entity;

import jakarta.persistence.*;
import lombok.*;

// LeaveType entity defining leave categories and default annual quotas
@Entity
@Table(name = "leave_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "default_annual_quota", nullable = false)
    private Integer defaultAnnualQuota;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    // Flag indicating whether leave requests for this type require manager approval
    @Column(name = "requires_approval", nullable = false)
    @Builder.Default
    private Boolean requiresApproval = true;
}
