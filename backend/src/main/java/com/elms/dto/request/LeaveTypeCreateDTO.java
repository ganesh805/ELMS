package com.elms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveTypeCreateDTO {

    @NotBlank(message = "Leave type name is required")
    private String name;

    @NotNull(message = "Default annual quota is required")
    @Min(value = 1, message = "Quota must be at least 1 day")
    private Integer defaultAnnualQuota;

    private String description;

    private Boolean requiresApproval = true;
}
