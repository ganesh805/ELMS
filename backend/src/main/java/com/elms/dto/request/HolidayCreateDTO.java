package com.elms.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HolidayCreateDTO {

    @NotNull(message = "Holiday date is required")
    @FutureOrPresent(message = "Holiday date must be today or in the future")
    private LocalDate date;

    @NotBlank(message = "Holiday name is required")
    private String name;

    private String description;
}
