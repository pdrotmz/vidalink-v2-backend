package com.vidalink.healthcare.entity.application.usecase;

import com.vidalink.healthcare.identity.application.dto.request.LoginRequest;
import com.vidalink.healthcare.identity.application.dto.response.LoginResponse;
import com.vidalink.healthcare.identity.application.usecase.LoginUseCase;
import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.exception.InvalidCredentialsException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginUseCase useCase;


    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest(
                "pedro@gmail.com",
                "pedrotomaz123"
        );

        User user = new User();

        user.setEmail("pedro@gmail.com");
        user.setPassword("encrypted");
        user.setRole(UserRole.CLIENT);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        LoginResponse response = useCase.execute(request);

        assertEquals("jwt-token", response.token());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationFails() {
        LoginRequest request = new LoginRequest(
                "pedro@gmail.com",
                "pedrotomaz123"
        );

        User user = new User();

        user.setEmail("pedrotomaz@gmail.com");
        user.setPassword("encrypted");
        user.setRole(UserRole.CLIENT);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new InvalidCredentialsException("invalid credentials"));

        assertThrows(InvalidCredentialsException.class,
                () -> useCase.execute(request));
    }
}
