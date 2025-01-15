package ru.yandex.practicum.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tag {

    private Long id;
    private String title;

    public Tag(String title) {
        this.title = title;
    }
}
