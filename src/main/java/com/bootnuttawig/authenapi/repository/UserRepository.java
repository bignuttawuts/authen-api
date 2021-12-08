package com.bootnuttawig.authenapi.repository;

import com.bootnuttawig.authenapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
