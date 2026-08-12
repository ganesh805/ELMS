package com.elms.dto.response;

import com.elms.entity.enums.LeaveStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LeaveRequestDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long leaveTypeId;
    private String leaveTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfDays;
    private String reason;
    private LeaveStatus status;
    private String attachmentFileName;
    private LocalDateTime appliedOn;
    private Long approverId;
    private String approverName;
    private String decisionComment;
    private LocalDateTime decisionDate;
}
