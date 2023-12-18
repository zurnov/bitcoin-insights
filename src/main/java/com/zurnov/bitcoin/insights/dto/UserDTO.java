package com.zurnov.bitcoin.insights.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime dateOfBirth;
    private LocalDateTime registrationDate;

}

