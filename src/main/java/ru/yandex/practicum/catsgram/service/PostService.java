package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();
    private final UserService userService;
    private int size = 10;
    private String sort = "asc";
    private int page = 1;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(int size, String sort, int page) {
        List<Post> subPost = new ArrayList<>(posts);
        if (posts.size() > 0 || posts.size() >= page) {
            int from = posts.size() <= page ? posts.size() - 1 : page -1;
            int to = posts.size() >= from + size ? from + size : posts.size();

            if (size == 10 && page == 1 && "default".equals(sort)) {
                subPost = posts.subList(from, to);
            } else {
                if (this.size == size && this.page == (page - size) && sort.equals(this.sort)) {
                    this.page = page;
                    subPost = posts.subList(from, to);
                } else {
                    this.size = size;
                    this.page = page;
                    this.sort = sort;

                    subPost = posts.subList(from, to);
                }
            }

            if ("desc".equals(sort)) {
                Collections.sort(subPost, Collections.reverseOrder());
            } else {
                Collections.sort(subPost);
            }
        }
        return subPost;
    }

    public Post create(Post post) {
        String email = post.getAuthor();
        User user = userService.findUserByEmail(email);
        if (user != null) {
            throw new UserNotFoundException("Пользователь " + email + " уже существует");
        }
        posts.add(post);
        return post;
    }

    public Post update(Post post) {
        String email = post.getAuthor();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь " + email + " не найден");
        }
        posts.add(post);
        return post;
    }

    public Optional<Post> findById(int postId) {
        return posts.stream()
                .filter(x -> x.getId() == postId)
                .findFirst();
    }
}