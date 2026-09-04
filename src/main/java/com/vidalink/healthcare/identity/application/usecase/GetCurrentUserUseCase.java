package com.vidalink.healthcare.identity.application.usecase;

import com.vidalink.healthcare.identity.application.dto.response.MeResponse;
import com.vidalink.healthcare.identity.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentUserUseCase {

    public MeResponse execute(User user) {
        return new MeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getRole()
        );
    }
}
