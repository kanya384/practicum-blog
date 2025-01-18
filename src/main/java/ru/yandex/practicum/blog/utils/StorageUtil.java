package ru.yandex.practicum.blog.utils;

import org.springframework.web.multipart.MultipartFile;

public interface StorageUtil {
    String store(MultipartFile file);
}
