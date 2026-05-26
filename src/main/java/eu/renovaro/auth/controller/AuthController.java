package eu.renovaro.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.renovaro.auth.domain.AuthResponse;
import eu.renovaro.auth.service.AuthService;
import eu.renovaro.auth.domain.LoginRequest;
import eu.renovaro.user.domain.SpecialistRegisterRequest;
import eu.renovaro.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/public/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public AuthController(AuthService authService, UserService userService,ObjectMapper objectMapper) {
        this.authService = authService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "/register/specialist", consumes = "multipart/form-data")
    public ResponseEntity<Void> registerSpecialist(
            @RequestPart("payload") String payload,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        SpecialistRegisterRequest request = objectMapper.readValue(payload, SpecialistRegisterRequest.class);
        userService.registerSpecialist(request, file);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/register/client")
//    public void registerClient(@RequestBody ClientRegisterRequest dto) {
//        // TODO
//    }
//    @PostMapping("/register/company")
//    public void registerCompany(@RequestBody CompanyRegisterRequest dto) {
//        // TODO
//    }
}

