package ru.yandex.practicum.blog.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.blog.repository.impl.JdbcNativeCommentRepository;
import ru.yandex.practicum.blog.repository.mappers.CommentRowMapper;

@Configuration
@Import({JdbcNativeCommentRepository.class, CommentRowMapper.class})
public class CommentRepositoryConfiguration {
}
