package com.elms.service;

import com.elms.dto.response.HolidayDTO;
import com.elms.mapper.EntityMapper;
import com.elms.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    @Transactional(readOnly = true)
    public List<HolidayDTO> getHolidaysByYear(Integer year) {
        if (year == null) {
            return holidayRepository.findAll().stream()
                    .map(EntityMapper::toHolidayDTO)
                    .toList();
        }
        return holidayRepository.findByYear(year).stream()
                .map(EntityMapper::toHolidayDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HolidayDTO> getUpcomingHolidays() {
        return holidayRepository.findByDateAfter(LocalDate.now()).stream()
                .map(EntityMapper::toHolidayDTO)
                .toList();
    }
}
