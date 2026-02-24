package com.habittracker.service;

import com.habittracker.domain.User;
import com.habittracker.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    
    // Explicit constructor
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userMapper.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    @Transactional
    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }
    
    @Transactional
    public User updateUser(User user) {
        userMapper.update(user);
        return userMapper.findById(user.getId()).orElse(null);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        userMapper.delete(id);
    }
}
