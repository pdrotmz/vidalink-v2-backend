package com.vidalink.healthcare.identity.application.usecase;

import com.vidalink.healthcare.identity.application.dto.request.RegisterRequest;
import com.vidalink.healthcare.identity.application.dto.response.UserResponse;
import com.vidalink.healthcare.identity.application.mapper.UserMapper;
import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.exception.CpfAlreadyExistsException;
import com.vidalink.healthcare.identity.domain.exception.EmailAlreadyExistsException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse execute(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByCpf(request.cpf())) {
            throw new CpfAlreadyExistsException("CPF already exists.");
        }

        User user = UserMapper.toEntity(request);

        if (request.email().toLowerCase().endsWith("@vidalink.com")) {
            user.setRole(UserRole.ADMIN);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);

        return UserMapper.toResponse(saved);
    }
}
