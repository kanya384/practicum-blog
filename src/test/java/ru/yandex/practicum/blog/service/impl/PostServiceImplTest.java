package ru.yandex.practicum.blog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.blog.dto.post.CreatePostDTO;
import ru.yandex.practicum.blog.dto.post.EditPostDTO;
import ru.yandex.practicum.blog.dto.post.PostDetailedDTO;
import ru.yandex.practicum.blog.dto.post.PostsPageDTO;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.PostRepository;
import ru.yandex.practicum.blog.service.CommentService;
import ru.yandex.practicum.blog.service.PostService;
import ru.yandex.practicum.blog.service.TagService;
import ru.yandex.practicum.blog.utils.StorageUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PostServiceImpl.class)
public class PostServiceImplTest {

    @MockitoBean
    PostRepository postRepository;

    @MockitoBean
    private CommentService commentsService;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private StorageUtil storageUtil;

    @Autowired
    PostService postService;

    @BeforeEach
    void setUp() {
        reset(postRepository);
        reset(commentsService);
        reset(tagService);
        reset(storageUtil);
    }

    @Test
    void readPosts_shouldReadPostsWithoutSearchFilter() {
        when(postRepository.findAll(any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(new Post("title", "image", "content"),
                        new Post("title", "image", "content")));

        when(postRepository.totalCount())
                .thenReturn(2);

        PostsPageDTO posts = postService.readPosts(null, 0, 10);
        assertEquals(2, posts.getPosts().size());
        assertEquals(2, posts.getTotalCount());
    }

    @Test
    void readPosts_shouldReadPostsWithSearchFilter() {
        when(postRepository.findByTag(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(new Post("title", "image", "content"),
                        new Post("title", "image", "content")));

        when(postRepository.totalCount(any(String.class)))
                .thenReturn(2);

        PostsPageDTO posts = postService.readPosts("tag", 0, 10);
        assertEquals(2, posts.getPosts().size());
        assertEquals(2, posts.getTotalCount());
    }

    @Test
    void readPostById_shouldReadPostsById() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(new Post(1L, "title", "image", "content", List.of(new Tag("tag")), 10, 2)));

        when(commentsService.readCommentsOfPost(1L))
                .thenReturn(List.of(new Comment(1L, 1L, "comment - 1"), new Comment(1L, 1L, "comment - 1")));

        PostDetailedDTO post = postService.readPostById(1L);

        assertEquals(1L, post.getId());
        assertEquals("title", post.getTitle());
        assertEquals("image", post.getImage());
        assertEquals("content", post.getContent());
        assertEquals(1, post.getTags().size());
        assertEquals(2, post.getComments().size());
        assertEquals(2, post.getLikes());
    }

    @Test
    void save_shouldCreatePost() {
        CreatePostDTO dto = new CreatePostDTO("title", "content",
                new MockMultipartFile("image", "image.jpg", "image/jpg",
                        "some image".getBytes()), List.of("tag - 1", "tag - 2"));

        when(postRepository.save(any(Post.class)))
                .thenReturn(new Post(1L, "title", "image", "content", List.of(new Tag("tag")), 0, 0));

        postService.save(dto);

        verify(postRepository, times(1)).save(any(Post.class));
        verify(tagService, times(1)).addTagsToPost(any(Long.class), anyList());
    }

    @Test
    void update_shouldUpdatePost() {
        EditPostDTO dto = new EditPostDTO("title", "content",
                new MockMultipartFile("image", "image.jpg", "image/jpg",
                        "some image".getBytes()), List.of("tag - 1", "tag - 2"));

        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Post(1L, "old title", "old image", "old content", List.of(new Tag("tag")), 0, 0)));

        postService.update(1L, dto);

        verify(postRepository, times(1)).update(any(Post.class));
        verify(tagService, times(1)).addTagsToPost(any(Long.class), anyList());
        verify(storageUtil, times(1)).store(any(MockMultipartFile.class));
    }

    @Test
    void deletePost_shouldDeletePost() {
        postService.deletePost(1L);
        verify(postRepository, times(1))
                .deleteById(any());
    }

    @Test
    void addLikeToPost_shouldAddLike() {
        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Post(1L, "old title", "old image", "old content", List.of(new Tag("tag")), 0, 1)));

        when(postRepository.update(any(Post.class)))
                .thenReturn(new Post(1L, "old title", "old image", "old content", List.of(new Tag("tag")), 0, 2));

        Post post = postService.addLikeToPost(1L);

        assertEquals(2, post.getLikes());
    }

}
