package ru.yandex.practicum.blog.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Tag;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativeTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(
                "select id, title from tags",
                mapper
        );
    }

    @Override
    public void save(Tag tag) {
        jdbcTemplate.update("insert into tags(title) values (?)",
                tag.getTitle()
        );
    }
}
