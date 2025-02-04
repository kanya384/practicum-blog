package ru.yandex.practicum.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.repository.CommentRepository;
import ru.yandex.practicum.blog.repository.PostRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void createComment_shouldCreateComment() throws Exception {
        Post post = new Post("post", "image", "content");
        postRepository.save(post);

        mockMvc.perform(post("/comments")
                        .header("referer", "/posts/" + post.getId())
                        .param("postId", post.getId().toString())
                        .param("content", "content")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId()));
    }

    @Test
    void editComment_shouldEditComment() throws Exception {
        Post post = new Post("post", "image", "content");

        postRepository.save(post);

        Comment comment = new Comment(post.getId(), "comment");

        commentRepository.save(comment);

        mockMvc.perform(post("/comments/{id}/edit", comment.getId())
                        .header("referer", "/posts/" + post.getId())
                        .param("content", "content")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId()));
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
