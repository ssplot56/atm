package com.splot.atm.config;

import com.splot.atm.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String ADMIN = Role.RoleName.ADMIN.name();
    public static final String USER = Role.RoleName.USER.name();
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts", "/atms").hasAnyRole(ADMIN, USER)
                .antMatchers(HttpMethod.POST, "/accounts/new-account",
                        "/accounts/transfer", "/atms/{atmId}/deposit",
                        "/atms/{atmId}/withdraw").hasAnyRole(ADMIN, USER)
                .antMatchers(HttpMethod.POST, "/atms",
                        "/atms/{atmId}/denomination").hasRole(ADMIN)
                .antMatchers(HttpMethod.PUT,
                        "/atms/{{atmId}}/denomination/{{atmDenominationId}}").hasRole(ADMIN)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .headers()
                .frameOptions().disable();
        return http.build();
    }
}
