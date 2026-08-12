package com.elms.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveBalanceDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long leaveTypeId;
    private String leaveTypeName;
    private Integer year;
    private Integer allocated;
    private Integer used;
    private Integer remaining;
}
