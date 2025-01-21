package ru.yandex.practicum.blog.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativeTagRepository extends JdbcBaseRepository<Tag> implements TagRepository {


    public JdbcNativeTagRepository(JdbcTemplate jdbc, RowMapper<Tag> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Tag> findAll() {
        return findMany(
                "select id, title from tags"
        );
    }

    @Override
    public Optional<Tag> findTagByTitle(String title) {
        return findOne("select id, title from tags where title = ?", title);
    }

    @Override
    public void save(Tag tag) {
        long id = insert("insert into tags(title) values (?)",
                tag.getTitle()
        );
        tag.setId(id);
    }

    @Override
    public void linkTagToPost(Long tagId, Long postId) {
        update(
                "insert into post_tags (tag_id, post_id) values (?, ?);", tagId, postId);
    }

    @Override
    public void unlinkTagFromPost(Long tagId, Long postId) {
        delete(
                "delete from post_tags where tag_id = ? and post_id = ?;", tagId, postId);
    }

    @Override
    public void unlinkAllTagsFromPost(Long postId) {
        delete(
                "delete from post_tags where post_id = ?;", postId);
    }
}