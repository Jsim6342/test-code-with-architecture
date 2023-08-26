package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreateDto;
import com.example.demo.post.domain.PostUpdateDto;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;

import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    public Post getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public Post create(PostCreateDto postCreateDto) {
        User user = userRepository.getById(postCreateDto.getWriterId());
        Post post = Post.from(postCreateDto, user, clockHolder);
        return postRepository.save(post);
    }

    public Post update(long id, PostUpdateDto postUpdateDto) {
        Post post = getById(id);
        post = post.update(postUpdateDto, clockHolder);
        return postRepository.save(post);
    }
}