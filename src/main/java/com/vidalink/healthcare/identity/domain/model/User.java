package com.vidalink.healthcare.identity.domain.model;

import com.vidalink.healthcare.identity.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "user")
@Table(name = "tb_user")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 16, nullable = false, unique = true, updatable = false)
    private UUID id;

    @Column(name = "name", length = 60, nullable = false, updatable = true)
    private String name;

    @Column(name = "email", length = 100, nullable = false, updatable = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false, updatable = true)
    private String password;

    @Column(name = "cpf", length = 11, nullable = false, unique = true, updatable = false)
    private String cpf;

    @Column(name = "role", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
