package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDetailedDTO {

    private Long userId;
    private String username;
    private String roleName;
    private String description;

}
