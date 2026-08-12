package com.elms.mapper;

import com.elms.dto.response.HolidayDTO;
import com.elms.dto.response.LeaveBalanceDTO;
import com.elms.dto.response.LeaveTypeDTO;
import com.elms.dto.response.UserDTO;
import com.elms.entity.Holiday;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveType;
import com.elms.entity.User;

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
}
