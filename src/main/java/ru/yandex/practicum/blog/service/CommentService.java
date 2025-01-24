package ru.yandex.practicum.blog.service;

import ru.yandex.practicum.blog.dto.comment.CreateCommentDTO;
import ru.yandex.practicum.blog.dto.comment.EditCommentDTO;
import ru.yandex.practicum.blog.model.Comment;

import java.util.List;

public interface CommentService {
    Comment save(CreateCommentDTO data);

    Comment update(Long id, EditCommentDTO data);

    List<Comment> readCommentsOfPost(Long postId);

    void delete(Long commentId);
}