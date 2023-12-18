package com.zurnov.bitcoin.insights.dto;

import lombok.Data;

@Data
public class UserRoleDTO {
    private Long userRoleId;
    private Long userId;
    private Long roleId;
    //TODO might include UserDTO and RoleDTO objects here instead of just IDs for clearer representation
}

