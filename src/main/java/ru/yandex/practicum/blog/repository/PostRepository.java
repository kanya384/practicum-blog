package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long id);

    List<Post> findAll(int offset, int limit);


    List<Post> findByTag(String search, int offset, int limit);

    int totalCount();

    int totalCount(String search);

    Post save(Post post);

    Post update(Post post);

    void deleteById(Long postId);
}
