package ru.yandex.practicum.blog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.repository.config.RepositoryConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = RepositoryConfiguration.class)
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativePostRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM comments");

        /* insert posts */
        jdbcTemplate.execute(
                "insert into posts(id, title, image, content, likes) " +
                        "values (-1, 'post - 1', 'image - 1', 'content - 1', 0);");

        jdbcTemplate.execute(
                "insert into posts(id, title, image, content, likes) " +
                        "values (-2, 'post - 2', 'image - 2', 'content - 2', 0);");

        jdbcTemplate.execute(
                "insert into posts(id, title, image, content, likes) " +
                        "values (-3, 'post - 3', 'image - 3', 'content - 3', 0);");

        /* insert tags */
        jdbcTemplate.execute(
                "insert into tags(id, title) values (1, 'tag - 1');");

        jdbcTemplate.execute(
                "insert into tags(id, title) values (2, 'tag - 2');");

        /* link tags to posts */
        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-1, 1);");

        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-1, 2);");

        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-2, 2);");

        /*insert post */
        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (1, -1, 'test comment');");
    }

    @Test
    void save_shouldAddPostToDatabase() {
        //Очищаем базу, а то будет конфликт присвоения id для новой записи
        jdbcTemplate.execute("DELETE FROM posts");

        Post post = new Post("post - 4", "image - 4", "content - 4");

        postRepository.save(post);

        Post savedPost = postRepository.findAll(0, 999).stream()
                .filter(createdPosts -> createdPosts.getId().equals(post.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedPost);

        assertNotNull(savedPost.getId());
        assertEquals("post - 4", savedPost.getTitle());
        assertEquals("image - 4", savedPost.getImage());
        assertEquals("content - 4", savedPost.getContent());
        assertEquals(0, savedPost.getLikes());
    }

    @Test
    void update_shouldUpdatePostLikesToDatabase() {
        Post post = postRepository.findById(-1L).orElse(null);
        assertNotNull(post);

        post.setLikes(post.getLikes() + 1);
        postRepository.update(post);

        Post savedPost = postRepository.findAll(0, 999).stream()
                .filter(createdPosts -> createdPosts.getId().equals(post.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(savedPost);

        assertEquals(1, savedPost.getLikes());
    }

    @Test
    void findById_shouldReturnPostByIdWithTags() {
        Optional<Post> post = postRepository.findById(-1L);
        assertTrue(post.isPresent());
        assertEquals(-1L, post.get().getId());
        assertEquals(2, post.get().getTags().size());
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        jdbcTemplate.execute("DELETE FROM posts");
        List<Post> posts = postRepository.findAll(0, 10);

        assertNotNull(posts);
        assertEquals(0, posts.size());
    }

    @Test
    void findAll_shouldReturnThreePosts() {
        List<Post> posts = postRepository.findAll(0, 10);

        assertNotNull(posts);
        assertEquals(3, posts.size());
    }

    @Test
    void findAll_shouldReturnOnePost() {
        List<Post> posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals(1, posts.size());
    }

    @Test
    void findAll_shouldContainTagsInPosts() {
        List<Post> posts = postRepository.findAll(0, 10);

        assertNotNull(posts);

        Post post = posts.stream()
                .filter(createdPosts -> createdPosts.getId().equals(-1L))
                .findFirst()
                .orElse(null);

        assertNotNull(post);
        assertEquals(2, post.getTags().size());
    }

    @Test
    void findAll_shouldContainCommentsCountInPosts() {
        List<Post> posts = postRepository.findAll(0, 10);

        assertNotNull(posts);

        Post post = posts.stream()
                .filter(createdPosts -> createdPosts.getId().equals(-1L))
                .findFirst()
                .orElse(null);

        assertNotNull(post);
        assertEquals(1, post.getCommentsCount());
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase() {
        postRepository.deleteById(-1L);

        List<Post> posts = postRepository.findAll(0, 10);

        Post deletedPost = posts.stream()
                .filter(createdPosts -> createdPosts.getId().equals(-1L))
                .findFirst()
                .orElse(null);

        assertNull(deletedPost);
    }

    @Test
    void totalCount_shouldReturnCountOfPosts() {
        int count = postRepository.totalCount();
        assertEquals(3, count);
    }

    @Test
    void totalCount_shouldReturnCountOfPostsWithTag() {
        int count = postRepository.totalCount("tag");
        assertEquals(2, count);
    }
}