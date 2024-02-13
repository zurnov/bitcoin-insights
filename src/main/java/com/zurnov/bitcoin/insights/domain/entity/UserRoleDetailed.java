package com.zurnov.bitcoin.insights.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRoleDetailed {
    private Long userId;
    private String username;
    private String roleName;
    private String description;
}
