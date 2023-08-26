package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreateDto;
import com.example.demo.post.domain.PostUpdateDto;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        TestClockHolder testClockHolder = new TestClockHolder(123456789L);

        this.postService = PostService.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(testClockHolder)
                .build();

        User user1 = fakeUserRepository.save(User.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("address1")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build());
        User user2 = fakeUserRepository.save(User.builder()
                .id(2L)
                .email("email2@naver.com")
                .nickname("nickname2")
                .address("address2")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .lastLoginAt(100L)
                .build());
        fakePostRepository.save(Post.builder()
                        .id(1L)
                        .content("hello")
                        .createdAt(100L)
                        .modifiedAt(100L)
                        .writer(user1)
                .build());
    }


    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postService.getById(1);

        // then
        assertThat(result.getContent()).isEqualTo("hello");
        assertThat(result.getWriter().getEmail()).isEqualTo("email1@naver.com");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
            .writerId(1)
            .content("foobar")
            .build();

        // when
        Post result = postService.create(postCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isEqualTo(123456789L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
            .content("hello world :)")
            .build();

        // when
        postService.update(1, postUpdateDto);

        // then
        Post post= postService.getById(1);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getModifiedAt()).isEqualTo(123456789L);
    }

}
