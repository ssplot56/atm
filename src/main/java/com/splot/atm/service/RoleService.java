package com.splot.atm.service;

import com.splot.atm.model.Role;

public interface RoleService {
    Role save(Role role);

    Role getById(Long id);

    Role getRoleByRoleName(Role.RoleName roleName);
}
