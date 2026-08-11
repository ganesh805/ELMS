package com.elms.repository;

import com.elms.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Repository interface for Holiday entity data access
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    // Find holiday by exact date
    Optional<Holiday> findByDate(LocalDate date);

    // Check if holiday exists on date
    boolean existsByDate(LocalDate date);

    // Find holidays falling between start date and end date
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
