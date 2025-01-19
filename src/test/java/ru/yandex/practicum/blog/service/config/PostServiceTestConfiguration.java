package ru.yandex.practicum.blog.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.repository.PostRepository;
import ru.yandex.practicum.blog.service.CommentsService;
import ru.yandex.practicum.blog.service.PostService;
import ru.yandex.practicum.blog.service.PostServiceImpl;
import ru.yandex.practicum.blog.service.TagService;
import ru.yandex.practicum.blog.utils.StorageUtil;

import static org.mockito.Mockito.mock;

@Configuration
public class PostServiceTestConfiguration {
    @Bean
    public PostService postService(PostRepository postRepository, CommentsService commentsService,
                                   TagService tagService, StorageUtil storageUtil) {
        return new PostServiceImpl(postRepository, commentsService, tagService, storageUtil);
    }

    @Bean
    public PostRepository postRepository() {
        return mock(PostRepository.class);
    }

    @Bean
    public StorageUtil storageUtil() {
        return mock(StorageUtil.class);
    }

    @Bean
    public CommentsService commentsService() {
        return mock(CommentsService.class);
    }

    @Bean
    public TagService tagService() {
        return mock(TagService.class);
    }
}
