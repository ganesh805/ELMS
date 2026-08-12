package com.elms.mapper;

import com.elms.dto.response.*;
import com.elms.entity.*;

public class EntityMapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .department(user.getDepartment())
                .dateOfJoining(user.getDateOfJoining())
                .managerId(user.getManager() != null ? user.getManager().getId() : null)
                .managerName(user.getManager() != null ? user.getManager().getFullName() : null)
                .active(true)
                .build();
    }

    public static LeaveTypeDTO toLeaveTypeDTO(LeaveType type) {
        if (type == null) {
            return null;
        }
        return LeaveTypeDTO.builder()
                .id(type.getId())
                .name(type.getName())
                .defaultAnnualQuota(type.getDefaultAnnualQuota())
                .description(type.getDescription())
                .active(type.getActive())
                .requiresApproval(type.getRequiresApproval())
                .build();
    }

    public static HolidayDTO toHolidayDTO(Holiday holiday) {
        if (holiday == null) {
            return null;
        }
        return HolidayDTO.builder()
                .id(holiday.getId())
                .date(holiday.getDate())
                .name(holiday.getName())
                .description(holiday.getDescription())
                .build();
    }

    public static LeaveBalanceDTO toLeaveBalanceDTO(LeaveBalance balance) {
        if (balance == null) {
            return null;
        }
        return LeaveBalanceDTO.builder()
                .id(balance.getId())
                .userId(balance.getUser() != null ? balance.getUser().getId() : null)
                .userName(balance.getUser() != null ? balance.getUser().getFullName() : null)
                .leaveTypeId(balance.getLeaveType() != null ? balance.getLeaveType().getId() : null)
                .leaveTypeName(balance.getLeaveType() != null ? balance.getLeaveType().getName() : null)
                .year(balance.getYear())
                .allocated(balance.getAllocated())
                .used(balance.getUsed())
                .remaining(balance.getRemaining())
                .build();
    }

    public static LeaveRequestDTO toLeaveRequestDTO(LeaveRequest request) {
        if (request == null) {
            return null;
        }
        return LeaveRequestDTO.builder()
                .id(request.getId())
                .userId(request.getUser() != null ? request.getUser().getId() : null)
                .userName(request.getUser() != null ? request.getUser().getFullName() : null)
                .leaveTypeId(request.getLeaveType() != null ? request.getLeaveType().getId() : null)
                .leaveTypeName(request.getLeaveType() != null ? request.getLeaveType().getName() : null)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .numberOfDays(request.getNumberOfDays())
                .reason(request.getReason())
                .status(request.getStatus())
                .attachmentFileName(request.getAttachmentFileName())
                .appliedOn(request.getAppliedOn())
                .approverId(request.getApprover() != null ? request.getApprover().getId() : null)
                .approverName(request.getApprover() != null ? request.getApprover().getFullName() : null)
                .decisionComment(request.getDecisionComment())
                .decisionDate(request.getDecisionDate())
                .build();
    }
}
