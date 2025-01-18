package ru.yandex.practicum.blog.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.model.Tag;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {

        Post post = new Post(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("image"),
                rs.getString("content"),
                new ArrayList<>(),
                hasColumn(rs, "comments_count") ? rs.getInt("comments_count") : 0,
                rs.getInt("likes")
        );

        try {
            do {
                long id = rs.getLong("tag_id");
                if (id == 0) {
                    continue;
                }
                Tag tag = new Tag(
                        id,
                        rs.getString("tag_title")
                );
                post.getTags().add(tag);
            } while (rs.next());
        } catch (SQLException e) {
            //do nothing
        }


        return post;
    }

    private static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x).toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}