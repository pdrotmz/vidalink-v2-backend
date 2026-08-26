package com.vidalink.healthcare.identity.presentation.controller;

import com.vidalink.healthcare.identity.application.dto.request.LoginRequest;
import com.vidalink.healthcare.identity.application.dto.request.RegisterRequest;
import com.vidalink.healthcare.identity.application.dto.response.LoginResponse;
import com.vidalink.healthcare.identity.application.usecase.LoginUseCase;
import com.vidalink.healthcare.identity.application.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Identity - Authentication",
        description = "Endpoints to user authenticate"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    @Operation(summary = "Sign up user", description = "Where user register on vidalink.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User signed up vidalink"),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, user not signed in.")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        registerUserUseCase.execute(request);
    }

    @Operation(summary = "Sign in user", description = "Where user login on vidalink.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "User sign in vidalink."),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, user not signed in.")
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return loginUseCase.execute(request);
    }
}
