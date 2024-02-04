package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRoleDetailedDTO {

    private Long userId;
    private String username;
    private String roleName;
    private String description;

}
