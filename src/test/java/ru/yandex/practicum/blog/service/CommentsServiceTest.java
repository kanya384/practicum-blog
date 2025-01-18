package ru.yandex.practicum.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.dto.comment.CreateCommentDTO;
import ru.yandex.practicum.blog.dto.comment.EditCommentDTO;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {CommentsServiceTest.TestConfig.class, CommentsServiceImpl.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentsServiceTest {
    @Autowired
    private CommentsService commentsService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        reset(commentRepository);
    }

    @Test
    void save_shouldSaveComment() {
        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(new Comment(1L, 1L, "test"));

        CreateCommentDTO dto = new CreateCommentDTO(1L, "test");
        Comment comment = commentsService.save(dto);

        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("test", comment.getContent());
    }

    @Test
    void update_shouldUpdateComment() {
        when(commentRepository.findCommentById(1L))
                .thenReturn(Optional.of(new Comment(1L, 1L, "test")));

        when(commentRepository.update(Mockito.any(Comment.class)))
                .thenReturn(new Comment(1L, 1L, "test - 2"));

        EditCommentDTO dto = new EditCommentDTO("test - 2");

        Comment comment = commentsService.update(1L, dto);

        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("test - 2", comment.getContent());
    }

    @Test
    void readCommentsOfPost_shouldReturnComments() {
        when(commentRepository.commentsOfPost(1L))
                .thenReturn(List.of(new Comment(1L, "test - 1"), new Comment(2L, "test - 2")));

        List<Comment> comments = commentsService.readCommentsOfPost(1L);
        assertEquals(2, comments.size());
    }

    @Test
    void delete_shouldDeleteComment() {
        commentsService.delete(1L);
        verify(commentRepository, times(1))
                .deleteById(Mockito.any());
    }

    @Configuration
    static class TestConfig {
        @Bean
        public CommentsService commentsService(CommentRepository commentRepository) {
            return new CommentsServiceImpl(commentRepository);
        }

        @Bean
        public CommentRepository commentRepository() {
            return mock(CommentRepository.class);
        }
    }
}
