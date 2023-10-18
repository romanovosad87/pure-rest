package com.example._5rest.service;

import com.example._5rest.model.User;
import com.example._5rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User get(UUID id) {
        return userRepository.get(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
