package eu.renovaro.auth.service.impl;

import eu.renovaro.auth.domain.AuthResponse;
import eu.renovaro.auth.domain.LoginRequest;
import eu.renovaro.user.mapper.UserMapper;
import eu.renovaro.user.repository.UserRepository;
import eu.renovaro.security.config.JWTTokenProvider;
import eu.renovaro.auth.service.AuthService;
import eu.renovaro.user.repository.UserRoleRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final static UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final UserRoleRepository userRoleRepository;


    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider
            , UserRoleRepository userRoleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRoleRepository = userRoleRepository;
    }
    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return new AuthResponse(token);
    }
}
