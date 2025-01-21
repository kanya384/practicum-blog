package ru.yandex.practicum.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.TagRepository;
import ru.yandex.practicum.blog.service.TagService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public void addTagsToPost(Long postId, List<String> tags) {
        for (String tagTitle : tags) {
            Optional<Tag> maybeTag = tagRepository.findTagByTitle(tagTitle);
            if (maybeTag.isPresent()) {
                tagRepository.linkTagToPost(maybeTag.get().getId(), postId);
                continue;
            }

            Tag tag = new Tag(tagTitle);

            tagRepository.save(tag);
            tagRepository.linkTagToPost(tag.getId(), postId);
        }
    }

    @Override
    public void unlinkAllTagsFromPost(Long postId) {
        tagRepository.unlinkAllTagsFromPost(postId);
    }
}
