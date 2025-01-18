package ru.yandex.practicum.blog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativeCommentRepository extends JdbcBaseRepository<Comment> implements CommentRepository {

    public JdbcNativeCommentRepository(JdbcTemplate jdbc, RowMapper<Comment> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        return findOne("select id, post_id, content from comments where id = ?", id);
    }

    @Override
    public List<Comment> commentsOfPost(Long postId) {
        return findMany("select id, post_id, content from comments where post_id = ?", postId);
    }

    @Override
    public Integer commentsOfPostCount(Long postId) {
        return jdbc.queryForObject("select count(*) from comments where post_id = ?", Integer.class, postId);
    }

    @Override
    public void save(Comment comment) {
        long id = insert("insert into comments(post_id, content) values (?, ?)",
                comment.getPostId(), comment.getContent()
        );

        comment.setId(id);
    }

    @Override
    public void update(Comment comment) {
        update("update comments set content = ? where id = ?",
                comment.getContent(), comment.getId());
    }

    @Override
    public void deleteById(Long commentId) {
        delete("delete from comments where id = ?", commentId);
    }
}