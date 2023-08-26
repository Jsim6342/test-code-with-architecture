package com.example.demo.post.controller;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreateDto;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatusCode;

import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PostCreateControllerTest {


    @Test
    void 사용자는_게시물을_작성할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123456789L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build());

        PostCreateDto postCreateDto = PostCreateDto.builder()
                .writerId(1)
                .content("hello")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController
                .createPost(postCreateDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getContent()).isEqualTo("hello");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("nickname1");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(123456789L);

    }
}
