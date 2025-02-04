package ru.yandex.practicum.blog.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.TagRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TagRepositoryImplTest {
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
                        "values (-1, 'post - 1', 'image - 1', 'content - 1', 0);");

        /* insert tags */
        jdbcTemplate.execute(
                "insert into tags(id, title) values (-1, 'tag - 1');");

        jdbcTemplate.execute(
                "insert into tags(id, title) values (-2, 'tag - 2');");

        /* link tags to posts */
        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-1, -1);");

        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-1, -2);");
    }

    @Test
    void save_shouldAddTagToDatabase() {

        Tag tag = new Tag("tag - 3");

        tagRepository.save(tag);

        Tag savedTag = tagRepository.findAll().stream()
                .filter(createdPosts -> createdPosts.getId().equals(tag.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedTag);
        assertNotNull(savedTag.getId());

        assertEquals("tag - 3", savedTag.getTitle());
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
