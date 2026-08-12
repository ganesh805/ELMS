package com.elms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceAdjustDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Leave type ID is required")
    private Long leaveTypeId;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Allocated quota is required")
    @Min(value = 0, message = "Allocated quota cannot be negative")
    private Integer allocated;
}
