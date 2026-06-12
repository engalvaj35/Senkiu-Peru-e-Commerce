package com.senkiu.auth.service;

import com.senkiu.auth.dto.AuthResponse;
import com.senkiu.auth.dto.LoginRequest;
import com.senkiu.auth.dto.RegisterRequest;

public interface AuthService {
    void login(LoginRequest request);
    
    AuthResponse register(RegisterRequest request);
}
