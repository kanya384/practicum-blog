package ru.yandex.practicum.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.TagRepository;
import ru.yandex.practicum.blog.service.config.TagServiceTestConfiguration;
import ru.yandex.practicum.blog.service.impl.TagServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {TagServiceTestConfiguration.class, TagServiceImpl.class})
public class TagServiceTest {
    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        reset(tagRepository);
    }

    @Test
    void addTagsToPost_shouldAddNewTagsToPost() {
        List<String> tags = List.of("tag - 1", "tag - 2", "tag - 3");
        Mockito.when(tagRepository.findTagByTitle(Mockito.any(String.class)))
                .thenReturn(Optional.empty());
        tagService.addTagsToPost(1L, tags);
        verify(tagRepository, times(tags.size())).save(Mockito.any(Tag.class));
        verify(tagRepository, times(tags.size())).linkTagToPost(Mockito.any(), Mockito.any());
    }

    @Test
    void addTagsToPost_shouldAddExistingTagsToPost() {
        List<String> tags = List.of("tag - 4");
        Mockito.when(tagRepository.findTagByTitle(Mockito.any(String.class)))
                .thenReturn(Optional.of(new Tag(1L, "tag - 4")));

        tagService.addTagsToPost(1L, tags);

        verify(tagRepository, times(0)).save(Mockito.any(Tag.class));
        verify(tagRepository, times(tags.size())).linkTagToPost(Mockito.any(), Mockito.any());
    }

    @Test
    void unlinkAllTagsFromPost_shouldUnlinkAllTags() {
        tagService.unlinkAllTagsFromPost(1L);

        verify(tagRepository, times(1)).unlinkAllTagsFromPost(1L);
    }
}
