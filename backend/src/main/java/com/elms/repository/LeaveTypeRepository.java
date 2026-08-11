package com.elms.repository;

import com.elms.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for LeaveType entity data access
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    // Find leave type by unique name
    Optional<LeaveType> findByName(String name);

    // Find all active leave types
    List<LeaveType> findByActiveTrue();
}
