package ru.yandex.practicum.blog.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Comment;

import java.util.List;

@Repository
@AllArgsConstructor
public class JdbcNativeCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Comment> mapper;

    @Override
    public List<Comment> commentsOfPost(Long postId) {
        return jdbcTemplate.query("select id, post_id, content from comments where post_id = ?", mapper, postId);
    }

    @Override
    public Integer commentsOfPostCount(Long postId) {
        return jdbcTemplate.queryForObject("select count(*) from comments where post_id = ?", Integer.class, postId);
    }

    @Override
    public void save(Comment comment) {
        jdbcTemplate.update("insert into comments(post_id, content) values (?, ?)",
                comment.getPostId(), comment.getContent()
        );
    }

    @Override
    public void deleteById(Long commentId) {
        jdbcTemplate.update("delete from comments where id = ?", commentId);
    }
}
