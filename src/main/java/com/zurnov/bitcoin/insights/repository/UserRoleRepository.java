package com.zurnov.bitcoin.insights.repository;

import com.zurnov.bitcoin.insights.domain.entity.Role;
import com.zurnov.bitcoin.insights.domain.entity.UserRole;
import com.zurnov.bitcoin.insights.dto.UserRoleDetailedDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRole ur WHERE ur.role.roleId = :roleId AND ur.user.userId = :userId")
    boolean existsByUserIdAndRoleID(@Param("roleId") Long roleId, @Param("userId") Long userID);

    @Query("SELECT new com.zurnov.bitcoin.insights.dto.UserRoleDetailedDTO(u.userId, u.username, r.roleName, r.description) " +
            "FROM User u JOIN UserRole ur ON u.userId = ur.user.userId JOIN Role r ON ur.role.roleId = r.roleId " +
            "WHERE (:roleId IS NULL OR r.roleId = :roleId)")
    List<UserRoleDetailedDTO> getUserRolesDetailed(@Param("roleId") Long roleId);

    List<UserRole> findByRole(Role role);



}
