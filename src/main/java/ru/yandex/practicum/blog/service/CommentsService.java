package ru.yandex.practicum.blog.service;

import ru.yandex.practicum.blog.dto.comment.CreateCommentDTO;
import ru.yandex.practicum.blog.dto.comment.EditCommentDTO;
import ru.yandex.practicum.blog.model.Comment;

public interface CommentsService {
    Comment save(CreateCommentDTO data);

    Comment update(Long id, EditCommentDTO data);

    void delete(Long commentId);
}
