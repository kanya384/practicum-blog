package ru.yandex.practicum.blog.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.repository.CommentRepository;
import ru.yandex.practicum.blog.service.CommentsService;
import ru.yandex.practicum.blog.service.CommentsServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class CommentServiceTestConfig {
    @Bean
    public CommentsService commentsService(CommentRepository commentRepository) {
        return new CommentsServiceImpl(commentRepository);
    }

    @Bean
    public CommentRepository commentRepository() {
        return mock(CommentRepository.class);
    }
}
