package com.elms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveTypeUpdateDTO {
    private String name;
    private Integer defaultAnnualQuota;
    private String description;
    private Boolean requiresApproval;
    private Boolean active;
}
