package ApiGymorEjecucion.Api.presentation.controller;

import ApiGymorEjecucion.Api.application.dto.request.auth.LoginRequest;
import ApiGymorEjecucion.Api.application.dto.response.auth.LoginResponse;
import ApiGymorEjecucion.Api.application.usecase.auth.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.ejecutar(request));
    }
}