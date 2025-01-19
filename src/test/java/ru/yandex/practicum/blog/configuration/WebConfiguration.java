package ru.yandex.practicum.blog.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.yandex.practicum.blog.service.config.CommentServiceTestConfiguration;
import ru.yandex.practicum.blog.service.config.PostServiceTestConfiguration;
import ru.yandex.practicum.blog.service.config.TagServiceTestConfiguration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.yandex.practicum.blog"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {CommentServiceTestConfiguration.class, PostServiceTestConfiguration.class, TagServiceTestConfiguration.class}
        )})
public class WebConfiguration {
}

