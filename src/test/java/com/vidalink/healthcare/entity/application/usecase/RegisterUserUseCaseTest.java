package com.vidalink.healthcare.entity.application.usecase;

import com.vidalink.healthcare.identity.application.dto.request.RegisterRequest;
import com.vidalink.healthcare.identity.application.usecase.RegisterUserUseCase;
import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.exception.CpfAlreadyExistsException;
import com.vidalink.healthcare.identity.domain.exception.EmailAlreadyExistsException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase useCase;


    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );


        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.existsByCpf(request.cpf())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encrypted-password");

        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Pedro", savedUser.getName());
        assertEquals("pedro@gmail.com", savedUser.getEmail());
        assertEquals("encrypted-password", savedUser.getPassword());
        assertEquals("12345678900", savedUser.getCpf());
        assertEquals(UserRole.CLIENT, savedUser.getRole());

        verify(passwordEncoder).encode(request.password());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        when(repository.existsByEmail(request.email())).thenReturn(true);
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class, () -> useCase.execute(request)
        );

        assertEquals("Email already exists.", exception.getMessage());

        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        when(repository.existsByCpf(request.cpf())).thenReturn(true);
        CpfAlreadyExistsException exception = assertThrows(
                CpfAlreadyExistsException.class, () -> useCase.execute(request)
        );

        assertEquals("CPF already exists.", exception.getMessage());
        verify(repository, never()).save(any(User.class));
    }
}
