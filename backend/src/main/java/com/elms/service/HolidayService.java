package com.elms.service;

import com.elms.dto.request.HolidayCreateDTO;
import com.elms.dto.response.HolidayDTO;
import com.elms.entity.Holiday;
import com.elms.exception.BusinessRuleException;
import com.elms.exception.ResourceNotFoundException;
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
    public List<HolidayDTO> getAllHolidays(Integer year) {
        if (year != null) {
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = LocalDate.of(year, 12, 31);
            return holidayRepository.findByDateBetween(startDate, endDate).stream()
                    .map(EntityMapper::toHolidayDTO)
                    .toList();
        }
        return holidayRepository.findAll().stream()
                .map(EntityMapper::toHolidayDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HolidayDTO> getHolidaysByYear(Integer year) {
        return getAllHolidays(year);
    }

    @Transactional(readOnly = true)
    public List<HolidayDTO> getUpcomingHolidays() {
        return holidayRepository.findByDateAfter(LocalDate.now()).stream()
                .map(EntityMapper::toHolidayDTO)
                .toList();
    }

    @Transactional
    public HolidayDTO createHoliday(HolidayCreateDTO dto) {
        if (holidayRepository.existsByDate(dto.getDate())) {
            throw new BusinessRuleException("A public holiday is already registered for date: " + dto.getDate());
        }

        Holiday holiday = Holiday.builder()
                .date(dto.getDate())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        Holiday saved = holidayRepository.save(holiday);
        return EntityMapper.toHolidayDTO(saved);
    }

    @Transactional
    public void deleteHoliday(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Holiday not found with id: " + id);
        }
        holidayRepository.deleteById(id);
    }
}
