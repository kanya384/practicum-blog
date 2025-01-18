package ru.yandex.practicum.blog.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.blog.repository.TagRepository;
import ru.yandex.practicum.blog.service.TagService;
import ru.yandex.practicum.blog.service.TagServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class TagServiceTestConfig {
    @Bean
    public TagService tagService(TagRepository tagRepository) {
        return new TagServiceImpl(tagRepository);
    }

    @Bean
    public TagRepository tagRepository() {
        return mock(TagRepository.class);
    }
}
