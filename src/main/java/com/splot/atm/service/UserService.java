package com.splot.atm.service;

import com.splot.atm.model.User;

public interface UserService {
    User save(User user);

    User findById(Long id);

    User findByEmail(String email);
}
