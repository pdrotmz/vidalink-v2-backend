package com.vidalink.healthcare.identity.application.usecase;

import com.vidalink.healthcare.identity.application.dto.response.UserResponse;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {

    private final UserRepository repository;

    public List<UserResponse> execute() {
        return repository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}
