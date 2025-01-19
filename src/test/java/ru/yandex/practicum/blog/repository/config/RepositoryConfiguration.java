package ru.yandex.practicum.blog.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.blog.configuration.DataSourceConfiguration;

@Configuration
@Import({DataSourceConfiguration.class, CommentRepositoryConfiguration.class,
        TagRepositoryConfiguration.class, PostRepositoryConfiguration.class})
public class RepositoryConfiguration {
}
