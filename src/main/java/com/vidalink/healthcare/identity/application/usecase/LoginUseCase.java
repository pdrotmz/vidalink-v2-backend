package com.vidalink.healthcare.identity.application.usecase;

import com.vidalink.healthcare.identity.application.dto.request.LoginRequest;
import com.vidalink.healthcare.identity.application.dto.response.LoginResponse;
import com.vidalink.healthcare.identity.domain.exception.InvalidCredentialsException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse execute(LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            User user = (User) authentication.getPrincipal();

            String token = jwtService.generateToken(user);

            return new LoginResponse(token);
        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException(exception.getMessage());
        }
    }
}
