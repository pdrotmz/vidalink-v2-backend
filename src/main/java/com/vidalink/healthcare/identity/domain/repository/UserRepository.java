package com.vidalink.healthcare.identity.domain.repository;

import com.vidalink.healthcare.identity.domain.model.User;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Configuration
public interface UserRepository {

    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    User save(User user);
}
