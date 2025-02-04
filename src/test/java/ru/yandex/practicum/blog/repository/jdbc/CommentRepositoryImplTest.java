package ru.yandex.practicum.blog.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class CommentRepositoryImplTest {

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
                        "values (-1, 'post - 1', 'image - 1', 'content - 1', 0);");

        /* insert comment */
        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (-1, -1, 'comment - 1');");

        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (-2, -1, 'comment - 2');");
    }

    @Test
    void save_shouldAddCommentToDatabase() {

        Comment comment = new Comment(-1L, "comment - 1");

        commentRepository.save(comment);

        Long commentId = comment.getId();

        Comment savedComment = commentRepository.commentsOfPost(comment.getPostId()).stream()
                .filter(createdPosts -> createdPosts.getId().equals(commentId))
                .findFirst()
                .orElse(null);

        assertNotNull(savedComment);

        assertEquals("comment - 1", savedComment.getContent());
    }

    @Test
    void commentsOfPost_shouldReturnCommentsOfPost() {
        List<Comment> comments = commentRepository.commentsOfPost(-1L);
        assertEquals(2, comments.size());
    }

    @Test
    void commentsOfPostCount_shouldReturnCommentsOfPost() {
        Integer count = commentRepository.commentsOfPostCount(-1L);
        assertEquals(2, count);
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase() {
        commentRepository.deleteById(-1L);
        commentRepository.deleteById(-2L);

        Integer count = commentRepository.commentsOfPostCount(-1L);

        assertEquals(0, count);
    }

}
