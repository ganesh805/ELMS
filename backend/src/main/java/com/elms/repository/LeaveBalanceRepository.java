package com.elms.repository;

import com.elms.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for LeaveBalance entity data access
@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    // Find specific balance by user, leave type, and year
    Optional<LeaveBalance> findByUserIdAndLeaveTypeIdAndYear(Long userId, Long leaveTypeId, Integer year);

    // Find all balances for a user in a given year
    List<LeaveBalance> findByUserIdAndYear(Long userId, Integer year);
}
