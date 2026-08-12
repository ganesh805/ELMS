package com.elms.service;

import com.elms.entity.Holiday;
import com.elms.exception.BusinessRuleException;
import com.elms.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkingDayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private WorkingDayService workingDayService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testMondayToFriday_Returns5Days() {
        LocalDate start = LocalDate.of(2026, 4, 6); // Monday
        LocalDate end = LocalDate.of(2026, 4, 10);  // Friday

        when(holidayRepository.findByDateBetween(start, end)).thenReturn(Collections.emptyList());

        int result = workingDayService.calculateWorkingDays(start, end);
        assertEquals(5, result);
    }

    @Test
    void testFridayToMonday_Returns2Days() {
        LocalDate start = LocalDate.of(2026, 4, 10); // Friday
        LocalDate end = LocalDate.of(2026, 4, 13);   // Monday

        when(holidayRepository.findByDateBetween(start, end)).thenReturn(Collections.emptyList());

        int result = workingDayService.calculateWorkingDays(start, end);
        assertEquals(2, result);
    }

    @Test
    void testWeekendOnly_Returns0Days() {
        LocalDate start = LocalDate.of(2026, 4, 11); // Saturday
        LocalDate end = LocalDate.of(2026, 4, 12);   // Sunday

        when(holidayRepository.findByDateBetween(start, end)).thenReturn(Collections.emptyList());

        int result = workingDayService.calculateWorkingDays(start, end);
        assertEquals(0, result);
    }

    @Test
    void testWithHoliday_ExcludesHoliday() {
        LocalDate start = LocalDate.of(2026, 4, 6); // Monday
        LocalDate end = LocalDate.of(2026, 4, 10);  // Friday
        LocalDate wednesdayHoliday = LocalDate.of(2026, 4, 8); // Wednesday

        Holiday holiday = Holiday.builder()
                .id(1L)
                .date(wednesdayHoliday)
                .name("Midweek Holiday")
                .build();

        when(holidayRepository.findByDateBetween(start, end)).thenReturn(List.of(holiday));

        int result = workingDayService.calculateWorkingDays(start, end);
        assertEquals(4, result);
    }

    @Test
    void testSingleDayLeave_Returns1Day() {
        LocalDate start = LocalDate.of(2026, 4, 6); // Monday
        LocalDate end = LocalDate.of(2026, 4, 6);   // Monday

        when(holidayRepository.findByDateBetween(start, end)).thenReturn(Collections.emptyList());

        int result = workingDayService.calculateWorkingDays(start, end);
        assertEquals(1, result);
    }

    @Test
    void testEndDateBeforeStartDate_ThrowsException() {
        LocalDate start = LocalDate.of(2026, 4, 10);
        LocalDate end = LocalDate.of(2026, 4, 6);

        assertThrows(BusinessRuleException.class, () -> workingDayService.calculateWorkingDays(start, end));
    }

    @Test
    void testIsWorkingDay_ReturnsTrueForRegularWeekday() {
        LocalDate monday = LocalDate.of(2026, 4, 6);
        when(holidayRepository.existsByDate(monday)).thenReturn(false);

        assertTrue(workingDayService.isWorkingDay(monday));
    }

    @Test
    void testIsWorkingDay_ReturnsFalseForWeekendAndHoliday() {
        LocalDate saturday = LocalDate.of(2026, 4, 11);
        LocalDate holidayDate = LocalDate.of(2026, 4, 8);
        when(holidayRepository.existsByDate(holidayDate)).thenReturn(true);

        assertFalse(workingDayService.isWorkingDay(saturday));
        assertFalse(workingDayService.isWorkingDay(holidayDate));
    }
}
