package com.hzcu.jellystudy.Service;

import com.hzcu.jellystudy.Api.UserService;
import com.hzcu.jellystudy.Entity.User;
import com.hzcu.jellystudy.Repository.UserRepository;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User updateLastLogin(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastLogin(new Date());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User addMoney(String userId, int amount) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setMoney(user.getMoney() + amount);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User deductMoney(String userId, int amount) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getMoney() >= amount) {
                user.setMoney(user.getMoney() - amount);
                return userRepository.save(user);
            }
        }
        return null;
    }

    @Override
    public int getUserBalance(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(User::getMoney).orElse(0);
    }
} 