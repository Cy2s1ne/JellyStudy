package com.hzcu.jellystudy.Api;

import com.hzcu.jellystudy.Entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    // 基础CRUD操作
    User saveUser(User user);
    Optional<User> getUserById(String id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(String id);
    
    // 基于用户名的操作
    Optional<User> getUserByUsername(String username);
    boolean existsByUsername(String username);
    
    // 基于邮箱的操作
    Optional<User> getUserByEmail(String email);
    boolean existsByEmail(String email);
    
    // 基于角色的操作
    List<User> getUsersByRole(String role);
    
    // 登录相关操作
    User updateLastLogin(String userId);
    
    // 金钱相关操作
    User addMoney(String userId, int amount);
    User deductMoney(String userId, int amount);
    int getUserBalance(String userId);
}
