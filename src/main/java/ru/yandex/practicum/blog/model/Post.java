package ru.yandex.practicum.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Post {

    private Long id;
    private String title;
    private String image;
    private String content;
    private List<Tag> tags;
    private Integer likes;

    public Post(String title, String image, String content, List<Tag> tags, Integer likes) {
        this.title = title;
        this.image = image;
        this.content = content;
        this.tags = tags;
        this.likes = likes;
    }
}
