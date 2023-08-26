package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostTest {

    @Test
    public void PostCreate로_게시물을_만들_수_있다() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .writerId(1)
                .content("hello")
                .build();
        User user = User.builder()
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        Post post = Post.from(postCreateDto, user, new TestClockHolder(123456789L));

        // then
        assertThat(post.getContent()).isEqualTo("hello");
        assertThat(post.getCreatedAt()).isEqualTo(123456789L);
        assertThat(post.getWriter().getEmail()).isEqualTo("email1@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("nickname1");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }

    @Test
    public void PostUpdate로_게시물을_수정할_수_있다() {
        // given
        User user = User.builder()
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        Post post = Post.builder()
                .id(1L)
                .content("hello")
                .createdAt(100L)
                .modifiedAt(0L)
                .writer(user)
                .build();
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("helloworld")
                .build();

        // when
        post = post.update(postUpdateDto, new TestClockHolder(123456789L));

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getModifiedAt()).isEqualTo(123456789L);

    }
}
