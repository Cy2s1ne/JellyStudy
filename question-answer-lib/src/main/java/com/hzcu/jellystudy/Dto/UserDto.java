package com.hzcu.jellystudy.Dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "users")
public class UserDto {
    @Id
    private String id;

    private String username;

    private String email;

    private String role;

    private Date createdAt;

    private Date lastLogin;

    private int money;
}