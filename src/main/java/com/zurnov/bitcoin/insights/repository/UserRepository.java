package com.zurnov.bitcoin.insights.repository;

import com.zurnov.bitcoin.insights.domain.entity.User;
import com.zurnov.bitcoin.insights.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByUsernameContaining(String username);

    List<User> findAllByUserIdAndUsernameContaining(Long id, String username);


}
