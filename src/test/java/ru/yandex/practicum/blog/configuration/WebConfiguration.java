package ru.yandex.practicum.blog.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.yandex.practicum.blog.service.config.CommentServiceTestConfig;
import ru.yandex.practicum.blog.service.config.PostServiceTestConfig;
import ru.yandex.practicum.blog.service.config.TagServiceTestConfig;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.yandex.practicum.blog"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {CommentServiceTestConfig.class, PostServiceTestConfig.class, TagServiceTestConfig.class}
        )})
public class WebConfiguration {
}

