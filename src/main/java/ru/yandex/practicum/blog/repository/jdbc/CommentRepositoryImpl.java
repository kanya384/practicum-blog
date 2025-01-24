package ru.yandex.practicum.blog.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

public class CommentRepositoryImpl extends BaseRepository<Comment> implements CommentRepository {
    public CommentRepositoryImpl(JdbcTemplate jdbc, RowMapper<Comment> mapper) {
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
    public Comment save(Comment comment) {
        long id = insert("insert into comments(post_id, content) values (?, ?)",
                comment.getPostId(), comment.getContent()
        );

        comment.setId(id);

        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        update("update comments set content = ? where id = ?",
                comment.getContent(), comment.getId());

        return comment;
    }

    @Override
    public void deleteById(Long commentId) {
        delete("delete from comments where id = ?", commentId);
    }
}
