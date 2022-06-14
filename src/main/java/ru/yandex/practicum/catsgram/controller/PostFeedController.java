package ru.yandex.practicum.catsgram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.catsgram.model.FriendsParams;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostFeedController {
    PostService postService;

    public PostFeedController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/feed/friends")

    public List<Post> getFriends(@RequestBody String listFriend) {
        List<Post> resultPost = new ArrayList<>();
        System.out.println("listFriend = " + listFriend);
        try {
            var jsonString = new ObjectMapper().readValue(listFriend, String.class);
            var friendsParams = new ObjectMapper().readValue(jsonString, FriendsParams.class);
            if (friendsParams == null) {
                throw new IllegalArgumentException("Не верный запрос");
            }
            for (String friend : friendsParams.getFriends()) {
                resultPost.addAll(postService.findAllByUserEmail(friend
                        , friendsParams.getSize()
                        , friendsParams.getSort()));
            }
        } catch (JsonProcessingException err) {
            throw new IllegalArgumentException("Неверный запрос");
        }

        return resultPost;
    }
}
