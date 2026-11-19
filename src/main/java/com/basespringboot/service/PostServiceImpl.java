package com.basespringboot.service;

import com.basespringboot.dto.PostDto;
import com.basespringboot.entity.Post;
import com.basespringboot.entity.User;
import com.basespringboot.repository.PostRepository;
import com.basespringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(PostDto postDto) {
        if (postDto.getContent() == null || postDto.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content cannot be empty");
        }
        if (postDto.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author userId is required");
        }

        // Fetch author to bind the @ManyToOne relationship
        User author = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setAuthor(author);
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @Override
    public Post updatePost(Long id, PostDto postDto) {
        Post existingPost = getPostById(id);

        if (postDto.getContent() == null || postDto.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content cannot be empty");
        }
        existingPost.setContent(postDto.getContent());

        if (postDto.getUserId() != null) {
            User author = userRepository.findById(postDto.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
            existingPost.setAuthor(author);
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long id) {
        Post existingPost = getPostById(id);
        postRepository.delete(existingPost);
    }
}