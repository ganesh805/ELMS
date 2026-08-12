package com.elms.repository;

import com.elms.entity.LeaveRequest;
import com.elms.entity.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByUserId(Long userId);

    List<LeaveRequest> findByUserIdAndStatus(Long userId, LeaveStatus status);

    List<LeaveRequest> findByUserManagerIdAndStatus(Long managerId, LeaveStatus status);

    List<LeaveRequest> findByUserManagerId(Long managerId);

    @Query("SELECT r FROM LeaveRequest r WHERE r.user.id = :userId " +
           "AND r.status IN (:statuses) " +
           "AND r.startDate <= :endDate AND r.endDate >= :startDate")
    List<LeaveRequest> findOverlappingRequests(@Param("userId") Long userId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("statuses") List<LeaveStatus> statuses);
}
