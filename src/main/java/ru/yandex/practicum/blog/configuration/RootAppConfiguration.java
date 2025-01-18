package ru.yandex.practicum.blog.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RootAppConfiguration {
    @Value("${storage_location}")
    private String storageRootPath;
}
