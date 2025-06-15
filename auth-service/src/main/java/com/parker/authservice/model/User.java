package com.parker.authservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "USER_DETAILS")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @Column(length = 36, nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Column(length = 36, nullable = false, name = "USER_NAME")
    private String name;
    @Column(nullable = false, length = 24, name = "PASSWORD")
    private String password;
    @Column(nullable = false, length = 36, name = "EMAIL")
    private String email;
    @Column(name = "ROLE")
    private String role;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDate createdDate;
    @LastModifiedDate()
    @Column(nullable = false)
    private LocalDate modifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
