package org.oc.paymybuddy.controller;

import jakarta.annotation.security.PermitAll;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@ControllerAdvice
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    public User signUp(@RequestBody User user) throws Exception {
        return userService.create(user);
    }

    @PutMapping
    public void update(@RequestBody User user) throws Exception {
        userService.update(user);
    }

    @DeleteMapping
    public void delete(@RequestBody User user) throws Exception {
        userService.delete(user);
    }

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.getUsers();
    }

}
