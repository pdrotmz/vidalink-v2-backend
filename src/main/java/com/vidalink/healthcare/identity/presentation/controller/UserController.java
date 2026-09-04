package com.vidalink.healthcare.identity.presentation.controller;

import com.vidalink.healthcare.identity.application.dto.response.MeResponse;
import com.vidalink.healthcare.identity.application.dto.response.UserResponse;
import com.vidalink.healthcare.identity.application.usecase.GetCurrentUserUseCase;
import com.vidalink.healthcare.identity.application.usecase.GetAllUsersUseCase;
import com.vidalink.healthcare.identity.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Identity - User",
        description = "Endpoints to user management"
)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;

    public UserController(GetCurrentUserUseCase getCurrentUserUseCase, GetAllUsersUseCase getAllUsersUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.getAllUsersUseCase = getAllUsersUseCase;
    }

    @Operation(summary = "Get user info", description = "Where user get their infos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User info successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user.")
    })
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public MeResponse me(@AuthenticationPrincipal User user) {
        return getCurrentUserUseCase.execute(user);
    }

    @Operation(summary = "Get all users", description = "Where admin get all users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user."),
            @ApiResponse(responseCode = "403", description = "User does not have permission to access this resource.")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAll() {
        return getAllUsersUseCase.execute();
    }
}
