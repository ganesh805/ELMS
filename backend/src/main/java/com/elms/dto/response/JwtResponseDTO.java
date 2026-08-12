package com.elms.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponseDTO {
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private UserDTO user;
}
