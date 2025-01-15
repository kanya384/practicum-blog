package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long id);

    List<Post> findAll(int offset, int limit);

    void save(Post post);

    void deleteById(Long postId);
}
