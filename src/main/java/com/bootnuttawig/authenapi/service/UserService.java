package com.bootnuttawig.authenapi.service;

import com.bootnuttawig.authenapi.entity.Role;
import com.bootnuttawig.authenapi.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);


}
