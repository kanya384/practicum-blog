package ru.yandex.practicum.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.repository.PostRepository;
import ru.yandex.practicum.blog.repository.TagRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void postsList_shouldReturnHtmlPosts() throws Exception {
        Post post1 = new Post("post - 1", "image - 1", "content - 1");
        Post post2 = new Post("post - 2", "image - 2", "content - 2");
        Post post3 = new Post("post - 3", "image - 3", "content - 3");
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("feed"))
                .andExpect(xpath("//div[contains(@class, \"post\")]").nodeCount(3));
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
        assertThrows(IllegalArgumentException.class, () -> mockMvc.perform(multipart("/posts")
                .file(null)
                .param("title", "title")
                .param("content", "content")
                .param("tags", "tag - 1", "tag - 2")
        ));
    }

    @Test
    void editPost_shouldEditPost() throws Exception {
        Post post = new Post("post", "image", "content");
        postRepository.save(post);

        MockMultipartFile file = new MockMultipartFile("image", "image.jpg", "image/jpg",
                "some image".getBytes());
        mockMvc.perform(multipart("/posts/{id}/edit", post.getId())
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
        Post post = new Post("post", "image", "content");
        postRepository.save(post);

        mockMvc.perform(post("/posts/{id}/like", post.getId())
                        .header("referer", "/posts/-1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/-1"));
    }

    @Test
    void readPostById_shouldReturn404OnUnknownPost() throws Exception {
        mockMvc.perform(get("/posts/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void readPostById_shouldReturnPost() throws Exception {
        Post post = new Post("post", "image", "content");
        postRepository.save(post);

        mockMvc.perform(get("/posts/{id}", post.getId()))
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
