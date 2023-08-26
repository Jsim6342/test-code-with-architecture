package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdateDto;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class PostControllerTest {


    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123456789L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .build();

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello")
                .writer(User.builder()
                        .id(1L)
                        .email("email1@naver.com")
                        .nickname("nickname1")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build())
                .build());

        // when
        ResponseEntity<PostResponse> result = testContainer.postController
                .getPostById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getContent()).isEqualTo("hello");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("nickname1");
    }

    @Test
    void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123456789L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .build();

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello")
                .writer(User.builder()
                        .id(1L)
                        .email("email1@naver.com")
                        .nickname("nickname1")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build())
                .build());

        // when
        // then
        assertThatThrownBy(() -> {
                    ResponseEntity<PostResponse> result = testContainer.postController
                            .getPostById(2);
                }
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123456789L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .build();

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello")
                .writer(User.builder()
                        .id(1L)
                        .email("email1@naver.com")
                        .nickname("nickname1")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build())
                .build());

        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("helloworld")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postController
                .updatePost(1, postUpdateDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("nickname1");
    }
}
