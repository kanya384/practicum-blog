package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<Tag> findAll();

    Optional<Tag> findTagByTitle(String title);

    void save(Tag tag);

    void linkTagToPost(Long tagId, Long postId);

    void unlinkTagFromPost(Long tagId, Long postId);

    void unlinkAllTagsFromPost(Long postId);
}
