package ru.yandex.practicum.blog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.config.RepositoryConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = RepositoryConfiguration.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativeTagRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");

        /* insert posts */
        jdbcTemplate.execute(
                "insert into posts(id, title, image, content, likes) " +
                        "values (1, 'post - 1', 'image - 1', 'content - 1', 0);");

        /* insert tags */
        jdbcTemplate.execute(
                "insert into tags(id, title) values (1, 'tag - 1');");

        jdbcTemplate.execute(
                "insert into tags(id, title) values (2, 'tag - 2');");

        /* link tags to posts */
        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (1, 1);");

        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (1, 2);");
    }

    @Test
    void save_shouldAddTagToDatabase() {
        //Очищаем базу, а то будет конфликт присвоения id для новой записи
        jdbcTemplate.execute("DELETE FROM tags");

        Tag tag = new Tag("tag - 1");

        tagRepository.save(tag);

        Tag savedTag = tagRepository.findAll().stream()
                .filter(createdPosts -> createdPosts.getId().equals(1L))
                .findFirst()
                .orElse(null);

        assertNotNull(savedTag);
        assertNotNull(savedTag.getId());

        assertEquals("tag - 1", savedTag.getTitle());
    }

    @Test
    void findAll_shouldReturnTags() {
        List<Tag> tags = tagRepository.findAll();
        assertEquals(2, tags.size());
    }

    @Test
    void findTagByTitle_shouldReturnTag() {
        Optional<Tag> maybeTag = tagRepository.findTagByTitle("tag - 1");
        assertTrue(maybeTag.isPresent());
    }
}