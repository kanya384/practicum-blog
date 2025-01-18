package ru.yandex.practicum.blog.service;

import ru.yandex.practicum.blog.dto.comment.CreateCommentDTO;
import ru.yandex.practicum.blog.dto.comment.EditCommentDTO;

public interface CommentsService {
    void save(CreateCommentDTO data);

    void update(Long id, EditCommentDTO data);

    void deleteComment(Long commentId);
}
