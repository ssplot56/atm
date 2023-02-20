package com.splot.atm.service;

import com.splot.atm.model.User;

public interface AuthenticationService {
    User register(String email, String password);
}
