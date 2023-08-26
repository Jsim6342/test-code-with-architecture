package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreateDto;
import com.example.demo.user.domain.UserUpdateDto;

public interface UserCreateService {

    User create(UserCreateDto userCreateDto);
}
