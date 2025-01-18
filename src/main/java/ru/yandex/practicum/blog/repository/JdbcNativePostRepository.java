package ru.yandex.practicum.blog.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
public class JdbcNativePostRepository extends JdbcBaseRepository<Post> implements PostRepository {

    public JdbcNativePostRepository(JdbcTemplate jdbc, RowMapper<Post> mapper) {
        super(jdbc, mapper);
    }

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
        return findOne("""
                        select p.id, p.title, p.image, p.content, p.likes, t.id as tag_id, t.title as tag_title 
                        from posts as p 
                        left join post_tags as pt on pt.post_id = p.id
                        left join tags as t on pt.tag_id = t.id
                        where p.id = ?
                        """,
                id);
    }

    @Override
    public List<Post> findAll(int offset, int limit) {
        List<Post> posts = findMany(
                """
                        select p.id, p.title, p.image, p.content, p.likes, sum(case when c.content is null then 0 else 1 end) as comments_count
                        from posts as p
                        left join comments as c on c.post_id = p.id
                        group by p.id
                        limit ?
                        offset ?
                        """,
                limit,
                offset
        );

        findTagsForPosts(posts);

        return posts;
    }

    @Override
    public List<Post> findByTag(String search, int offset, int limit) {
        List<Post> posts = findMany(
                """
                        select p.id, p.title, p.image, p.content, p.likes, sum(case when c.content is null then 0 else 1 end) as comments_count
                        from posts as p
                        left join comments as c on c.post_id = p.id
                        where p.id in (
                            select pt.post_id from tags as t
                            left join post_tags as pt on pt.tag_id = t.id
                            where lower(t.title) like ?
                        )
                        group by p.id
                        limit ?
                        offset ?
                        """,
                "%" + search.toLowerCase() + "%",
                limit,
                offset
        );

        findTagsForPosts(posts);

        return posts;
    }

    @Override
    public int totalCount() {
        Integer count = jdbc.queryForObject(
                "select count(*) as total from posts",
                Integer.class
        );
        return count == null ? 0 : count;
    }

    @Override
    public int totalCount(String search) {
        Integer count = jdbc.queryForObject(
                """
                        select count(*)
                        from posts as p
                        where p.id in (
                            select pt.post_id from tags as t
                            left join post_tags as pt on pt.tag_id = t.id
                            where lower(t.title) like ?
                        )
                        """,
                Integer.class,
                "%" + search.toLowerCase() + "%"
        );
        return count == null ? 0 : count;
    }

    @Override
    public void save(Post post) {
        long id = insert("insert into posts(title, image, content, likes) values (?, ?, ?, ?)",
                post.getTitle(), post.getImage(), post.getContent(), post.getLikes()
        );

        post.setId(id);
    }

    @Override
    public void update(Post post) {
        update("update posts set title = ?, image = ?, content = ?, likes = ? where id = ?",
                post.getTitle(), post.getImage(), post.getContent(), post.getLikes(), post.getId());
    }

    @Override
    public void deleteById(Long postId) {
        delete("delete from posts where id = ?", postId);
    }

    private void findTagsForPosts(List<Post> posts) {
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        List<PostTag> postTags = jdbc.query(con -> {
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