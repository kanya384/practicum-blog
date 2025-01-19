package ru.yandex.practicum.blog.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.blog.configuration.DataSourceConfiguration;
import ru.yandex.practicum.blog.configuration.WebConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitWebConfig({DataSourceConfiguration.class, WebConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Очистка и заполнение тестовых данных в базе
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM post_tags");

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
                "insert into tags(id, title) values (-1, 'tag - 1');");

        jdbcTemplate.execute(
                "insert into tags(id, title) values (-2, 'tag - 2');");

        /* link tags to posts */
        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-1, -1);");

        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-1, -2);");

        jdbcTemplate.execute(
                "insert into post_tags (post_id, tag_id) values (-2, -2);");

        /*insert post */
        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (-1, -1, 'test comment');");
    }

    @Test
    void postsList_shouldReturnHtmlPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(xpath("//div[contains(@class, \"post\")]").nodeCount(3))
                .andExpect(xpath("//div[contains(@class, \"post\")]//a").string("post - 3"));
    }

    @Test
    void createPost_shouldAddNewPost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes());

        mockMvc.perform(multipart("/posts")
                        .file(file)
                        .param("title", "title")
                        .param("content", "content")
                        .param("tags", "tag - 1", "tag - 2")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void createPost_shouldNotAddNewPostWithoutFile() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () -> mockMvc.perform(multipart("/posts")
                .file(null)
                .param("title", "title")
                .param("content", "content")
                .param("tags", "tag - 1", "tag - 2")
        ));
    }

    @Test
    void editPost_shouldEditPost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes());
        mockMvc.perform(multipart("/posts/{id}/edit", -1)
                        .file(file)
                        .param("title", "new-title")
                        .param("content", "new-content")
                        .param("tags", "tag - 1", "tag - 2")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void addLikeToPost_shouldAddLikeToPost() throws Exception {
        mockMvc.perform(post("/posts/{id}/like", -1)
                        .header("referer", "/posts/-1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/-1"));
    }

    @Test
    void readPostById_shouldReturn404OnUnknownPost() throws Exception {
        mockMvc.perform(get("/posts/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("404"));
    }

    @Test
    void readPostById_shouldReturnPost() throws Exception {
        mockMvc.perform(get("/posts/{id}", -1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"));
    }

    @Test
    void delete_shouldDeletePost() throws Exception {
        mockMvc.perform(post("/posts/{id}?_method=delete", -1)
                        .header("referer", "/posts/-1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }


}
