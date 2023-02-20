package com.splot.atm.service;

import com.splot.atm.model.User;

public interface UserService {
    User save(User user);

    User findUserByEmail(String email);

    User findByEmail(String email);
}
