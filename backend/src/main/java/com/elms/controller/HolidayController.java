package com.elms.controller;

import com.elms.dto.response.HolidayDTO;
import com.elms.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping
    public ResponseEntity<List<HolidayDTO>> getHolidays(@RequestParam(name = "year", required = false) Integer year) {
        return ResponseEntity.ok(holidayService.getHolidaysByYear(year));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<HolidayDTO>> getUpcomingHolidays() {
        return ResponseEntity.ok(holidayService.getUpcomingHolidays());
    }
}
