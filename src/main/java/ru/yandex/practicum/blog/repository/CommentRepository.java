package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findCommentById(Long id);

    List<Comment> commentsOfPost(Long postId);

    Integer commentsOfPostCount(Long postId);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void deleteById(Long commentId);

    void deleteAll();
}
