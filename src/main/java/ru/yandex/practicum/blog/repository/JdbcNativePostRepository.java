package ru.yandex.practicum.blog.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.model.Tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> mapper;

    @AllArgsConstructor
    @Getter
    @Setter
    private static class PostTag {
        private Long postId;
        private Long tagId;
        private String tagTitle;
    }

    @Override
    public Optional<Post> findById(Long id) {
        try {
            Post post = jdbcTemplate.queryForObject(
                    """
                            select p.id, p.title, p.image, p.content, p.likes, t.id as tag_id, t.title as tag_title 
                            from posts as p 
                            left join post_tags as pt on pt.post_id = p.id
                            left join tags as t on pt.tag_id = t.id
                            where p.id = ?
                            """,
                    mapper,
                    id
            );

            return Optional.ofNullable(post);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> findAll(int offset, int limit) {
        List<Post> posts = jdbcTemplate.query(
                "select id, title, image, content, likes from posts limit ? offset ?",
                mapper,
                limit,
                offset
        );

        findTagsForPosts(posts);

        return posts;
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update("insert into posts(title, image, content, likes) values (?, ?, ?, ?)",
                post.getTitle(), post.getImage(), post.getContent(), post.getLikes()
        );
    }

    @Override
    public void deleteById(Long postId) {
        jdbcTemplate.update("delete from posts where id = ?", postId);
    }

    private void findTagsForPosts(List<Post> posts) {
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        List<PostTag> postTags = jdbcTemplate.query(con -> {
            StringBuilder query = new StringBuilder();
            query.append("""
                    select p.id as post_id, t.id as tag_id, t.title as tag_title from post_tags as pt
                    left join tags as t on t.id = pt.tag_id
                    left join posts as p on p.id = pt.post_id
                    where p.id in (
                    """);

            for (int i = 0; i < postIds.size(); i++) {
                if (i == 0) {
                    query.append("?");
                    continue;
                }
                query.append(", ?");
            }
            query.append(")");

            PreparedStatement stmt = con.prepareStatement(query.toString());
            for (int i = 0; i < postIds.size(); i++) {
                stmt.setLong(i + 1, postIds.get(i));
            }

            return stmt;
        }, mapToPostTag);

        Map<Long, Post> postIdToPost = posts.stream()
                .collect(
                        Collectors.toMap(
                                Post::getId,
                                post -> post
                        )
                );
        for (PostTag postTag : postTags) {
            Post post = postIdToPost.get(postTag.getPostId());

            post.getTags()
                    .add(new Tag(postTag.getPostId(), postTag.getTagTitle()));
        }
    }

    private final RowMapper<PostTag> mapToPostTag = (ResultSet rs, int rowNum) -> new PostTag(
            rs.getLong("post_id"),
            rs.getLong("tag_id"),
            rs.getString("tag_title")
    );
}
