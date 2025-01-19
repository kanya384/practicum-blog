package ru.yandex.practicum.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.blog.configuration.DataSourceConfiguration;
import ru.yandex.practicum.blog.configuration.WebConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({DataSourceConfiguration.class, WebConfiguration.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();


        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM comments");

        /* insert posts */
        jdbcTemplate.execute(
                "insert into posts(id, title, image, content, likes) " +
                        "values (1, 'post - 1', 'image - 1', 'content - 1', 0);");

        /* insert comment */
        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (-1, 1, 'comment - 1');");

        jdbcTemplate.execute(
                "insert into comments(id, post_id, content) values (-2, 1, 'comment - 2');");
    }

    @Test
    void createComment_shouldCreateComment() throws Exception {
        mockMvc.perform(post("/comments")
                        .header("referer", "/posts/-1")
                        .param("postId", "1")
                        .param("content", "content")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/-1"));
    }

    @Test
    void editComment_shouldEditComment() throws Exception {
        mockMvc.perform(post("/comments/{id}/edit", -1)
                        .header("referer", "/posts/-1")
                        .param("content", "content")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/-1"));
    }

    @Test
    void deleteComment_shouldDeleteComment() throws Exception {
        mockMvc.perform(post("/comments/{id}?_method=delete", -1)
                        .header("referer", "/posts/-1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/-1"));
    }
}
