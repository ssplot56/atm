package com.splot.atm.config;

import com.splot.atm.model.Account;
import com.splot.atm.model.Atm;
import com.splot.atm.model.Denomination;
import com.splot.atm.model.Role;
import com.splot.atm.model.User;
import com.splot.atm.repository.DenominationRepository;
import com.splot.atm.service.AccountService;
import com.splot.atm.service.AtmService;
import com.splot.atm.service.RoleService;
import com.splot.atm.service.UserService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final RoleService roleService;
    private final UserService userService;
    private final AccountService accountService;
    private final AtmService atmService;
    private final DenominationRepository denominationRepository;

    public DataInitializer(RoleService roleService, UserService userService,
                           AccountService accountService,
                           AtmService atmService,
                           DenominationRepository denominationRepository) {
        this.roleService = roleService;
        this.userService = userService;
        this.accountService = accountService;
        this.atmService = atmService;
        this.denominationRepository = denominationRepository;
    }

    @PostConstruct
    public void inject() {

        Role adminRole = roleService.getRoleByRoleName(Role.RoleName.ADMIN);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setRoleName(Role.RoleName.ADMIN);
            roleService.save(adminRole);
        }
        Role userRole = roleService.getRoleByRoleName(Role.RoleName.USER);
        if (userRole == null) {
            userRole = new Role();
            userRole.setRoleName(Role.RoleName.USER);
            roleService.save(userRole);
        }

        if (userService.findByEmail("admin@mail.com") == null) {
            User admin = new User();
            admin.setEmail("admin@mail.com");
            admin.setPassword("password");
            admin.setRoles(Set.of(adminRole, userRole));
            admin = userService.save(admin);

            Account adminAccount = new Account();
            adminAccount.setBalance(BigDecimal.valueOf(500));
            adminAccount.setNumber("1111111111111111");
            adminAccount.setUser(admin);
            accountService.save(adminAccount);
        }
        if (userService.findByEmail("user@mail.com") == null) {
            User user = new User();
            user.setEmail("user@mail.com");
            user.setPassword("password");
            user.setRoles(Set.of(userRole));
            user = userService.save(user);

            Account userAccount = new Account();
            userAccount.setBalance(BigDecimal.valueOf(500));
            userAccount.setNumber("1010101010101010");
            userAccount.setUser(user);
            accountService.save(userAccount);
        }

        if (denominationRepository.findAll().isEmpty()) {
            Denomination hundred = new Denomination();
            hundred.setNominal(100);
            Denomination twoHundred = new Denomination();
            twoHundred.setNominal(200);
            Denomination fiveHundred = new Denomination();
            fiveHundred.setNominal(500);
            denominationRepository.saveAll(Arrays.asList(hundred,
                    twoHundred, fiveHundred));
        }

        if (atmService.findAll().isEmpty()) {
            Atm atm = new Atm();
            atm.setName("First ATM");
            atm = atmService.save(atm);

            atmService.addDenomination(atm.getId(), 100, 150);
            atmService.addDenomination(atm.getId(), 200, 100);
            atmService.addDenomination(atm.getId(), 500, 70);
        }
    }
}
