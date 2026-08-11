package com.elms.dto.response;

import com.elms.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String department;
    private LocalDate dateOfJoining;
    private Long managerId;
    private String managerName;
    private Boolean active;
}
