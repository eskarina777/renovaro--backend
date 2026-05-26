package eu.renovaro.auth.service;

import eu.renovaro.auth.domain.AuthResponse;
import eu.renovaro.auth.domain.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
}
