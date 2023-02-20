package com.splot.atm.service.impl;

import com.splot.atm.model.Role;
import com.splot.atm.model.User;
import com.splot.atm.service.AuthenticationService;
import com.splot.atm.service.RoleService;
import com.splot.atm.service.UserService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;

    public AuthenticationServiceImpl(UserService userService,
                                     RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
