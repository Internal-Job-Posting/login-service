package com.ukg.login_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ukg.login_service.dto.LoginRequest;
import com.ukg.login_service.dto.UserDetailsDTO;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    @PostMapping("/api/employee/verify")
    UserDetailsDTO verifyCredentials(@RequestBody LoginRequest loginRequest);
}
