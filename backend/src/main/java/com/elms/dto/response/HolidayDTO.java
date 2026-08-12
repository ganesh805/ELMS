package com.elms.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class HolidayDTO {
    private Long id;
    private LocalDate date;
    private String name;
    private String description;
}
