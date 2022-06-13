package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{postId}")
    public Optional<Post> findById(@PathVariable int postId) {
        return postService.findById(postId);
    }

    @GetMapping("/posts")
    public List<Post> findAll(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(defaultValue = "1") int page) {
        List<String> param = List.of("asc", "desc", "default");
        if (!param.contains(sort.toLowerCase()) || page <= 0 || size <= 0) {
            throw new IllegalArgumentException("sort only = asc | desc, page>=0, size >0");
        }
        return postService.findAll(size, sort, page);
    }

    @PostMapping(value = "/post")
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }
}