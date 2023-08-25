package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostResponseTest {

    @Test
    public void Post로_응답을_생성할_수_있다() {
        // given
        Post post = Post.builder()
                .content("hello")
                .writer(User.builder()
                        .email("email1@naver.com")
                        .nickname("nickname1")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build())
                .build();

        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getContent()).isEqualTo("hello");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("email1@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("nickname1");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
