package ru.yandex.practicum.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {

    private Long id;
    private Long postId;
    private String content;

    public Comment(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
