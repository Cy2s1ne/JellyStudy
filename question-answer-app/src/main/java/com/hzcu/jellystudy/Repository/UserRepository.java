package com.hzcu.jellystudy.Repository;

import com.hzcu.jellystudy.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    List<User> findByRole(String role);
} 