package com.ukg.login_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsDTO {
    private Long employeeId;
    private String email;
    private boolean authenticated;
    private List<String> authorities;
}
