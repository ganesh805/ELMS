package com.elms.service;

import com.elms.entity.Holiday;
import com.elms.exception.BusinessRuleException;
import com.elms.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkingDayService {

    private final HolidayRepository holidayRepository;

    @Transactional(readOnly = true)
    public int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BusinessRuleException("Start date and end date must not be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new BusinessRuleException("End date cannot be before start date");
        }

        Set<LocalDate> holidayDates = holidayRepository.findByDateBetween(startDate, endDate).stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        int workingDays = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            boolean isHoliday = holidayDates.contains(current);

            if (!isWeekend && !isHoliday) {
                workingDays++;
            }
            current = current.plusDays(1);
        }

        return workingDays;
    }
}
