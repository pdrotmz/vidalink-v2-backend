package com.vidalink.healthcare.identity.application.mapper;

import com.vidalink.healthcare.identity.application.dto.request.RegisterRequest;
import com.vidalink.healthcare.identity.application.dto.response.UserResponse;
import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(RegisterRequest request) {
        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setCpf(request.cpf());
        user.setRole(UserRole.CLIENT);

        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
