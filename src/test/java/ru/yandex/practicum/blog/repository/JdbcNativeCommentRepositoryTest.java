package ru.yandex.practicum.blog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.repository.config.RepositoryConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = RepositoryConfiguration.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativeCommentRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM comments");

        /* insert posts */
        jdbcTemplate.execute(
                "insert into posts(id, title, image, content, likes) " +
                        "values (1, 'post - 1', 'image - 1', 'content - 1', 0);");

        /* insert comment */
        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (1, 1, 'comment - 1');");

        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (2, 1, 'comment - 2');");
    }

    @Test
    void save_shouldAddCommentToDatabase() {
        //Очищаем базу, а то будет конфликт присвоения id для новой записи
        jdbcTemplate.execute("DELETE FROM comments");

        Comment comment = new Comment(1L, "comment - 1");

        commentRepository.save(comment);

        Long commentId = comment.getId();

        Comment savedComment = commentRepository.commentsOfPost(comment.getPostId()).stream()
                .filter(createdPosts -> createdPosts.getId().equals(commentId))
                .findFirst()
                .orElse(null);

        assertNotNull(savedComment.getId());
        assertNotNull(savedComment);

        assertEquals("comment - 1", savedComment.getContent());
    }

    @Test
    void commentsOfPost_shouldReturnCommentsOfPost() {
        List<Comment> comments = commentRepository.commentsOfPost(1L);
        assertEquals(2, comments.size());
    }

    @Test
    void commentsOfPostCount_shouldReturnCommentsOfPost() {
        Integer count = commentRepository.commentsOfPostCount(1L);
        assertEquals(2, count);
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase() {
        commentRepository.deleteById(1L);
        commentRepository.deleteById(2L);

        Integer count = commentRepository.commentsOfPostCount(1L);

        assertEquals(0, count);
    }
}