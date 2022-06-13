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

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(int size, String sort, int pageFrom) {
        List<Post> subPost = new ArrayList<>(posts);
        int postFrom = (pageFrom * size) + 1;
        if (postFrom <= posts.size() && (posts.size() > 0 || posts.size() >= postFrom)) {
            if ("desc".equals(sort)) {
                Collections.sort(subPost, Collections.reverseOrder());
            } else {
                Collections.sort(subPost);
            }

            int from = posts.size() <= postFrom ? posts.size() - 1 : postFrom - 1;
            int to = posts.size() >= from + size ? from + size : posts.size();

            subPost = subPost.subList(from, to);
        } else {
            subPost = new ArrayList<>();
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