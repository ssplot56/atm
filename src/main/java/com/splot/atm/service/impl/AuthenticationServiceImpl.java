package com.splot.atm.service.impl;

import com.splot.atm.model.Role;
import com.splot.atm.model.User;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.AuthenticationService;
import com.splot.atm.service.RoleService;
import com.splot.atm.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final AccountService accountService;

    public AuthenticationServiceImpl(UserService userService, RoleService roleService,
                                     AccountService accountService) {
        this.userService = userService;
        this.roleService = roleService;
        this.accountService = accountService;
    }

    @Override
    public User register(String email, String password) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByRoleName(Role.RoleName.USER));
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        userService.save(user);
        return user;
    }
}
