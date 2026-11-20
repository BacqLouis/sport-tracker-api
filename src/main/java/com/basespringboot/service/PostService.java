package com.basespringboot.service;

import com.basespringboot.dto.PostDto;
import com.basespringboot.entity.Post;
import java.util.List;

public interface PostService {
    Post createPost(PostDto postDto);
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Post updatePost(Long id, PostDto postDto);
    void deletePost(Long id);
}