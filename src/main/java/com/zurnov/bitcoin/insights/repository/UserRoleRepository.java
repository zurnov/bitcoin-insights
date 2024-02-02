package com.zurnov.bitcoin.insights.repository;

import com.zurnov.bitcoin.insights.domain.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRole ur WHERE ur.role.roleId = :roleId AND ur.user.userId = :userId")
    boolean existsByUserIdAndRoleID(@Param("roleId") Long roleId, @Param("userId") Long userID);
}
