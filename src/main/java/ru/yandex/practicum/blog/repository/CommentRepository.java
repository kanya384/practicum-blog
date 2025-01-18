package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findCommentById(Long id);

    List<Comment> commentsOfPost(Long postId);

    Integer commentsOfPostCount(Long postId);

    void save(Comment comment);

    void update(Comment comment);

    void deleteById(Long commentId);
}
