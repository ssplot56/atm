package com.splot.atm.controller;

import com.splot.atm.dto.request.UserRequestDto;
import com.splot.atm.dto.response.UserResponseDto;
import com.splot.atm.model.User;
import com.splot.atm.service.AuthenticationService;
import com.splot.atm.service.mapper.ResponseDtoMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    private final AuthenticationService authService;
    private final ResponseDtoMapper<UserResponseDto, User> userResponseMapper;

    public RegistrationController(AuthenticationService authService,
                                  ResponseDtoMapper<UserResponseDto, User> userResponseMapper) {
        this.authService = authService;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRequestDto requestDto) {
        User user = authService.register(requestDto.getEmail(), requestDto.getPassword());
        return userResponseMapper.mapToDto(user);
    }
}
