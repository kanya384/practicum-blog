package ru.yandex.practicum.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.blog.dto.post.CreatePostDTO;
import ru.yandex.practicum.blog.dto.post.EditPostDTO;
import ru.yandex.practicum.blog.dto.post.PostDetailedDTO;
import ru.yandex.practicum.blog.dto.post.PostsPageDto;
import ru.yandex.practicum.blog.exception.InternalServerException;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.CommentRepository;
import ru.yandex.practicum.blog.repository.PostRepository;
import ru.yandex.practicum.blog.repository.TagRepository;
import ru.yandex.practicum.blog.utils.StorageUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final StorageUtil storageUtil;

    public PostsPageDto readPosts(String search, int page, int limit) {
        PostsPageDto postsPageDto = PostsPageDto.builder()
                .page(page)
                .limit(limit)
                .build();

        page--;

        if (search != null && !search.isBlank()) {
            postsPageDto.setPosts(postRepository.findByTag(search, page, limit));
            postsPageDto.setTotalCount(postRepository.totalCount(search));
        } else {
            postsPageDto.setPosts(postRepository.findAll(page * limit, limit));
            postsPageDto.setTotalCount(postRepository.totalCount());
        }

        return postsPageDto;
    }

    @Override
    public Optional<PostDetailedDTO> readPostById(Long id) {
        Optional<Post> maybePost = postRepository.findById(id);
        if (maybePost.isEmpty()) {
            return Optional.empty();
        }

        Post post = maybePost.get();

        List<Comment> comments = commentRepository.commentsOfPost(post.getId());

        return Optional.of(PostDetailedDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(post.getImage())
                .likes(post.getLikes())
                .tags(post.getTags())
                .comments(comments)
                .build());
    }

    @Transactional
    public void save(CreatePostDTO data) {
        String imageFileName = storageUtil.store(data.getImage());

        Post post = new Post(data.getTitle(), imageFileName, data.getContent().replaceAll("\n", "<br />"));
        postRepository.save(post);

        for (String tagTitle : data.getTags()) {
            Optional<Tag> maybeTag = tagRepository.findTagByTitle(tagTitle);
            if (maybeTag.isPresent()) {
                tagRepository.linkTagToPost(maybeTag.get().getId(), post.getId());
                continue;
            }

            Tag tag = new Tag(tagTitle);

            tagRepository.save(tag);
            tagRepository.linkTagToPost(tag.getId(), post.getId());
        }
    }

    @Transactional
    @Override
    public void update(Long id, EditPostDTO data) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new InternalServerException("no post with id = " + id));

        tagRepository.unlinkAllTagsFromPost(post.getId());

        post.setTitle(data.getTitle());
        post.setContent(data.getContent());

        postRepository.update(post);

        if (!data.getImage().isEmpty()) {
            String imageFileName = storageUtil.store(data.getImage());
            post.setImage(imageFileName);
        }

        for (String tagTitle : data.getTags()) {
            Optional<Tag> maybeTag = tagRepository.findTagByTitle(tagTitle);
            if (maybeTag.isPresent()) {
                tagRepository.linkTagToPost(maybeTag.get().getId(), post.getId());
                continue;
            }

            Tag tag = new Tag(tagTitle);

            tagRepository.save(tag);
            tagRepository.linkTagToPost(tag.getId(), post.getId());
        }
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void addLikeToPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new InternalServerException("no post with id = " + id));

        post.setLikes(post.getLikes() + 1);

        postRepository.update(post);
    }
}
