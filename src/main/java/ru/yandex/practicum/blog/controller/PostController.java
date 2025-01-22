package ru.yandex.practicum.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.blog.dto.post.CreatePostDTO;
import ru.yandex.practicum.blog.dto.post.EditPostDTO;
import ru.yandex.practicum.blog.dto.post.PostDetailedDTO;
import ru.yandex.practicum.blog.service.PostService;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/**")
    public String postsList(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int limit, Model model) {
        model.addAttribute("result", postService.readPosts(search, page, limit));
        model.addAttribute("form", new CreatePostDTO());
        model.addAttribute("search", search);
        model.addAttribute("pageSizes", List.of(10, 20, 50));
        return "feed";
    }

    @PostMapping(path = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createPost(@ModelAttribute CreatePostDTO data) {
        postService.save(data);
        return "redirect:/posts";
    }

    @PostMapping(path = "/{id}/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editPost(@PathVariable("id") Long postId, @ModelAttribute EditPostDTO data) {
        postService.update(postId, data);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    public String addLikeToPost(@PathVariable("id") Long postId, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) {
        postService.addLikeToPost(postId);
        return "redirect:" + referrer;
    }

    @GetMapping("/{id}")
    public String readPostById(@PathVariable("id") Long id, Model model) {
        PostDetailedDTO post = postService.readPostById(id);

        model.addAttribute("post", post);
        model.addAttribute("form", new CreatePostDTO());
        return "post";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}