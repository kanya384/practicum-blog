package ru.yandex.practicum.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.blog.dto.post.CreatePostDTO;
import ru.yandex.practicum.blog.dto.post.EditPostDTO;
import ru.yandex.practicum.blog.dto.post.PostDetailedDTO;
import ru.yandex.practicum.blog.dto.post.PostsPageDTO;
import ru.yandex.practicum.blog.exception.PostNotFoundException;
import ru.yandex.practicum.blog.model.Comment;
import ru.yandex.practicum.blog.model.Post;
import ru.yandex.practicum.blog.repository.PostRepository;
import ru.yandex.practicum.blog.service.CommentService;
import ru.yandex.practicum.blog.service.PostService;
import ru.yandex.practicum.blog.service.TagService;
import ru.yandex.practicum.blog.utils.StorageUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentService commentsService;
    private final TagService tagService;
    private final StorageUtil storageUtil;

    public PostsPageDTO readPosts(String search, int page, int limit) {
        PostsPageDTO postsPageDto = PostsPageDTO.builder()
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
    public PostDetailedDTO readPostById(Long id) {
        Optional<Post> maybePost = postRepository.findById(id);
        if (maybePost.isEmpty()) {
            throw new PostNotFoundException("no post with id = " + id);
        }

        Post post = maybePost.get();

        List<Comment> comments = commentsService.readCommentsOfPost(post.getId());

        return PostDetailedDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(post.getImage())
                .likes(post.getLikes())
                .tags(post.getTags())
                .comments(comments)
                .build();
    }

    @Transactional
    public void save(CreatePostDTO data) {
        String imageFileName = storageUtil.store(data.getImage());

        Post post = new Post(data.getTitle(), imageFileName, data.getContent().replaceAll("\n", "<br />"));
        post = postRepository.save(post);

        tagService.addTagsToPost(post.getId(), data.getTags());
    }

    @Transactional
    @Override
    public void update(Long id, EditPostDTO data) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("no post with id = " + id));

        tagService.unlinkAllTagsFromPost(post.getId());

        post.setTitle(data.getTitle());
        post.setContent(data.getContent().replaceAll("\n", "<br />"));

        if (!data.getImage().isEmpty()) {
            String imageFileName = storageUtil.store(data.getImage());
            post.setImage(imageFileName);
        }

        postRepository.update(post);

        tagService.addTagsToPost(post.getId(), data.getTags());
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Post addLikeToPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("no post with id = " + id));

        post.setLikes(post.getLikes() + 1);

        return postRepository.update(post);
    }
}