package com.elms.dto.request;

import com.elms.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String fullName;
    private Role role;
    private String department;
    private Long managerId;
    private Boolean active;
}
