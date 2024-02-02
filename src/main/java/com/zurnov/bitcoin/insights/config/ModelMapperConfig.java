package com.zurnov.bitcoin.insights.config;

import com.zurnov.bitcoin.insights.domain.entity.RolePermission;
import com.zurnov.bitcoin.insights.domain.entity.UserRole;
import com.zurnov.bitcoin.insights.dto.RolePermissionDTO;
import com.zurnov.bitcoin.insights.dto.UserRoleDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<UserRole, UserRoleDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
            }
        });

        modelMapper.addMappings(new PropertyMap<RolePermission, RolePermissionDTO>() {
            @Override
            protected void configure() {
                map().setRolePermissionId(source.getRolePermissionId());
                map().setRoleId(source.getRole().getRoleId());
                map().setPermissionId(source.getPermission().getPermissionId());
            }
        });

        return modelMapper;
    }
}


