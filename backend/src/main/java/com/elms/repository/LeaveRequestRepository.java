package com.elms.repository;

import com.elms.entity.LeaveRequest;
import com.elms.entity.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repository interface for LeaveRequest entity data access
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // Find all leave requests for a given employee
    List<LeaveRequest> findByUserId(Long userId);

    // Find pending leave requests for direct reports of a manager
    List<LeaveRequest> findByUserManagerIdAndStatus(Long managerId, LeaveStatus status);

    // Find all leave requests for direct reports of a manager
    List<LeaveRequest> findByUserManagerId(Long managerId);

    // Check for overlapping PENDING or APPROVED leave requests for a user
    @Query("SELECT r FROM LeaveRequest r WHERE r.user.id = :userId " +
           "AND r.status IN (:statuses) " +
           "AND r.startDate <= :endDate AND r.endDate >= :startDate")
    List<LeaveRequest> findOverlappingRequests(@Param("userId") Long userId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("statuses") List<LeaveStatus> statuses);
}
