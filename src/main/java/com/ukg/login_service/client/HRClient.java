package com.ukg.login_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ukg.login_service.dto.LoginRequest;
import com.ukg.login_service.dto.UserDetailsDTO;


@FeignClient(name = "hr-service")
public interface HRClient {
    @PostMapping("/api/hr/verify")
    UserDetailsDTO verifyCredentials(@RequestBody LoginRequest loginRequest);
}
