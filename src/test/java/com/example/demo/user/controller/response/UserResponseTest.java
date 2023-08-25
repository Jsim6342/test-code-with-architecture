package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserResponseTest {

    @Test
    public void User으로_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build();

        // when
        UserResponse userResponse = UserResponse.from(user);

        // then
        assertThat(userResponse.getId()).isEqualTo(1);
        assertThat(userResponse.getEmail()).isEqualTo("email1@naver.com");
        assertThat(userResponse.getNickname()).isEqualTo("nickname1");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }
}
