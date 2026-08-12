package com.elms.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveTypeDTO {
    private Long id;
    private String name;
    private Integer defaultAnnualQuota;
    private String description;
    private Boolean active;
    private Boolean requiresApproval;
}
