package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdateDto;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
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

        // when
        ResponseEntity<UserResponse> result = testContainer.userController
                .getUserById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("email1@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("nickname1");
    }


    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        assertThatThrownBy(() -> {
            ResponseEntity<UserResponse> result = testContainer.userController
                    .getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        User user = testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build());

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        User user = testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickname("nickname1")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build());

        // when
        // then
        assertThatThrownBy(() -> {
           testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123456789L))
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

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
                .getMyInfo("email1@naver.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("email1@naver.com");
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getNickname()).isEqualTo("nickname1");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(123456789L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123456789L))
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

        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("update-nickname")
                .address("update_address")
                .build();

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
                .updateMyInfo("email1@naver.com", userUpdateDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getAddress()).isEqualTo("update_address");
        assertThat(result.getBody().getNickname()).isEqualTo("update-nickname");
    }


    // Stub 방식 예제
//    @Test
//    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() throws Exception {
//        // given
//        UserController userController = UserController.builder()
//                .userReadService(new UserReadService() {
//                                     @Override
//                                     public User getByEmail(String email) {
//                                         return null;
//                                     }
//
//                                     @Override
//                                     public User getById(long id) {
//                                         return User.builder()
//                                                 .id(id)
//                                                 .email("email1@naver.com")
//                                                 .nickname("nickname1")
//                                                 .address("Seoul")
//                                                 .status(UserStatus.ACTIVE)
//                                                 .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
//                                                 .lastLoginAt(100L)
//                                                 .build();
//                                     }
//                                 }
//
//                )
//                .build();
//
//        // when
//        ResponseEntity<UserResponse> result = userController.getUserById(1);
//
//        // then
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
//        assertThat(result.getBody()).isNotNull();
//        assertThat(result.getBody().getId()).isEqualTo(1);
//        assertThat(result.getBody().getEmail()).isEqualTo("email1@naver.com");
//        assertThat(result.getBody().getNickname()).isEqualTo("nickname1");
//    }

}
