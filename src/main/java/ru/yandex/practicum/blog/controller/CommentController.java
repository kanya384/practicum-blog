package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.blog.dto.comment.CreateCommentDTO;
import ru.yandex.practicum.blog.dto.comment.EditCommentDTO;
import ru.yandex.practicum.blog.service.CommentService;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentsService;

    @PostMapping
    public String createComment(@ModelAttribute CreateCommentDTO data, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        commentsService.save(data);
        return "redirect:" + referrer;
    }

    @PostMapping("/{id}/edit")
    public String editComment(@PathVariable(name = "id") Long id, @ModelAttribute EditCommentDTO data, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        commentsService.update(id, data);
        return "redirect:" + referrer;
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        commentsService.delete(id);
        return "redirect:" + referrer;
    }
}
