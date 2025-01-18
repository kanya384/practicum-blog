package ru.yandex.practicum.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.blog.model.Post;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsPageDto {
    private List<Post> posts;
    private int totalCount;
    private int page;
    private int limit;

    public boolean hasNextPage() {
        return (totalCount - page * limit) > 0;
    }

    public boolean hasPrevPage() {
        return page > 1;
    }
}
