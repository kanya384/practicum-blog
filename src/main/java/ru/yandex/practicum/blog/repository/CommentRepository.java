package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> commentsOfPost(Long postId);

    Integer commentsOfPostCount(Long postId);

    void save(Comment comment);

    void deleteById(Long commentId);
}
