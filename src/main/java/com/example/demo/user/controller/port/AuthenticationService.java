package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreateDto;
import com.example.demo.user.domain.UserUpdateDto;

public interface AuthenticationService {
    void login(long id);
    void verifyEmail(long id, String certificationCode);
}
