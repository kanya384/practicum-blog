package ru.yandex.practicum.blog.repository;

import ru.yandex.practicum.blog.model.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll();

    void save(Tag tag);
}
