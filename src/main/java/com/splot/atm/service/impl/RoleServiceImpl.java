package com.splot.atm.service.impl;

import com.splot.atm.model.Role;
import com.splot.atm.repository.RoleRepository;
import com.splot.atm.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.getReferenceById(id);
    }

    @Override
    public Role getRoleByRoleName(Role.RoleName roleName) {
        return roleRepository.getRoleByRoleName(roleName);
    }
}
