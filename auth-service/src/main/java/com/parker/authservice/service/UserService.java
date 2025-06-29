package com.parker.authservice.service;

import com.parker.authservice.model.User;
import com.parker.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> loadUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }
}
