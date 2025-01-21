package ru.yandex.practicum.blog.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.blog.repository.impl.JdbcNativeTagRepository;
import ru.yandex.practicum.blog.repository.mappers.TagRowMapper;

@Configuration
@Import({JdbcNativeTagRepository.class, TagRowMapper.class})
public class TagRepositoryConfiguration {
}
