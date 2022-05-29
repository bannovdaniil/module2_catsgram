package ru.yandex.practicum.catsgram.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> usersList = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUsersPage() {
        logger.trace("Количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public User create(@RequestBody User user) throws InvalidEmailException, UserAlreadyExistException {
        if (user == null || user.getEmail() == null) {
            throw new InvalidEmailException("Error: E-mail is null.");
        }
        if (usersList.contains(user)) {
            throw new UserAlreadyExistException("Error: User is exists");
        } else {
            logger.trace("Добавляем нового пользователя: {}", user);
            usersList.add(user);
        }
        return usersList.get(usersList.indexOf(user));
    }

    @PutMapping
    public User update(@RequestBody User user) throws InvalidEmailException {
        if (user == null || user.getEmail() == null) {
            throw new InvalidEmailException("Error: E-mail is null.");
        }
        if (usersList.contains(user)) {
            logger.trace("Изменение пользователя: {}", user);
            usersList.set(usersList.indexOf(user), user);
        } else {
            logger.trace("Добавляем нового пользователя: {}", user);
           usersList.add(user);
        }
        return usersList.get(usersList.indexOf(user));
    }
}
