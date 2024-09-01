package com.ukg.login_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ukg.login_service.client.EmployeeClient;
import com.ukg.login_service.client.HRClient;
import com.ukg.login_service.dto.LoginRequest;
import com.ukg.login_service.dto.UserDetailsDTO;
import com.ukg.login_service.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private HRClient hrClient;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmployeeClient employeeClient;

    public Map<String, Object> login(LoginRequest loginRequest) {
        // First, try to authenticate with HR client
        UserDetailsDTO userDetails = hrClient.verifyCredentials(loginRequest);
        
        if (!userDetails.isAuthenticated()) {
            // If HR authentication fails, try employee client
            userDetails = employeeClient.verifyCredentials(loginRequest);
        }
        
        if (userDetails.isAuthenticated()) {
            String token = jwtUtil.generateToken(userDetails.getEmail(), userDetails.getAuthorities());
            Map<String, Object> response = new HashMap<>();
            response.put("employeeId", userDetails.getEmployeeId());
            response.put("email", userDetails.getEmail());
            response.put("authorities", userDetails.getAuthorities());
            response.put("token", token);
            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
