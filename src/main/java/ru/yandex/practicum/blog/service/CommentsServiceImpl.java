package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.dto.comment.CreateCommentDTO;
import ru.yandex.practicum.blog.dto.comment.EditCommentDTO;
import ru.yandex.practicum.blog.exception.InternalServerException;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
    private final CommentRepository commentRepository;

    @Override
    public Comment save(CreateCommentDTO data) {
        Comment comment = new Comment(data.getPostId(), data.getContent());
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Long id, EditCommentDTO data) {
        Comment comment = commentRepository.findCommentById(id)
                .orElseThrow(() -> new InternalServerException("no comment with id = " + id));
        comment.setContent(data.getContent());
        commentRepository.update(comment);

        return comment;
    }

    @Override
    public List<Comment> readCommentsOfPost(Long postId) {
        return commentRepository.commentsOfPost(postId);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}