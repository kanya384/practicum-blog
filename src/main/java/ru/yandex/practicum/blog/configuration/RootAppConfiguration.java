package ru.yandex.practicum.blog.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Setter
public class RootAppConfiguration {
    @Value("${spring.web.resources.static-locations}")
    private List<String> storageRootPath;

    public String getStorageRootPath() {
        return storageRootPath.getFirst().replaceAll("file:", "");
    }
}
