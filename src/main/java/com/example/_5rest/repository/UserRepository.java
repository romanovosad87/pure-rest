package com.example._5rest.repository;

import com.example._5rest.model.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRepository {
    private final Map<UUID, User> userMap = new ConcurrentHashMap<>();

    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public User get(UUID id) {
        return userMap.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void delete(UUID id) {
        userMap.remove(id);
    }
}
