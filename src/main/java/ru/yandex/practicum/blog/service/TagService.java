package ru.yandex.practicum.blog.service;

import java.util.List;

public interface TagService {
    void addTagsToPost(Long postId, List<String> tags);

    void unlinkAllTagsFromPost(Long postId);
}