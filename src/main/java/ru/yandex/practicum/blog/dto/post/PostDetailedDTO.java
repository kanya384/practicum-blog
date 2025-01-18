package ru.yandex.practicum.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Tag;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailedDTO {
    private Long id;
    private String title;
    private String image;
    private String content;
    private List<Tag> tags;
    private List<Comment> comments;
    private Integer likes;
}
