package ru.yandex.practicum.blog.service;

import ru.yandex.practicum.blog.dto.post.CreatePostDTO;
import ru.yandex.practicum.blog.dto.post.EditPostDTO;
import ru.yandex.practicum.blog.dto.post.PostDetailedDTO;
import ru.yandex.practicum.blog.dto.post.PostsPageDto;
import ru.yandex.practicum.blog.model.Post;

public interface PostService {
    PostsPageDto readPosts(String search, int page, int limit);

    PostDetailedDTO readPostById(Long id);

    void save(CreatePostDTO post);

    void update(Long id, EditPostDTO post);

    void deletePost(Long id);

    Post addLikeToPost(Long id);
}
