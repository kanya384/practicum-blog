package ru.yandex.practicum.blog.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.blog.repository.impl.JdbcNativePostRepository;
import ru.yandex.practicum.blog.repository.mappers.PostRowMapper;

@Configuration
@Import({JdbcNativePostRepository.class, PostRowMapper.class})
public class PostRepositoryConfiguration {
}
