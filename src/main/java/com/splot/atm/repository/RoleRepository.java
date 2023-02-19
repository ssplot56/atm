package com.splot.atm.repository;

import com.splot.atm.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByRoleName(Role.RoleName roleName);
}
