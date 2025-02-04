package ru.yandex.practicum.blog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.blog.model.Tag;
import ru.yandex.practicum.blog.repository.TagRepository;
import ru.yandex.practicum.blog.service.TagService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = TagServiceImpl.class)
public class TagServiceImplTest {
    @Autowired
    private TagService tagService;

    @MockitoBean
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        reset(tagRepository);
    }

    @Test
    void addTagsToPost_shouldAddNewTagsToPost() {
        List<String> tags = List.of("tag - 1", "tag - 2", "tag - 3");
        when(tagRepository.findTagByTitle(any(String.class)))
                .thenReturn(Optional.empty());
        tagService.addTagsToPost(1L, tags);
        verify(tagRepository, times(tags.size())).save(any(Tag.class));
        verify(tagRepository, times(tags.size())).linkTagToPost(any(), any());
    }

    @Test
    void addTagsToPost_shouldAddExistingTagsToPost() {
        List<String> tags = List.of("tag - 4");
        when(tagRepository.findTagByTitle(any(String.class)))
                .thenReturn(Optional.of(new Tag(1L, "tag - 4")));

        tagService.addTagsToPost(1L, tags);

        verify(tagRepository, times(0)).save(any(Tag.class));
        verify(tagRepository, times(tags.size())).linkTagToPost(any(), any());
    }

    @Test
    void unlinkAllTagsFromPost_shouldUnlinkAllTags() {
        tagService.unlinkAllTagsFromPost(1L);

        verify(tagRepository, times(1)).unlinkAllTagsFromPost(1L);
    }
}
