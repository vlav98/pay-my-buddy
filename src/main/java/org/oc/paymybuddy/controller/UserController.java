package org.oc.paymybuddy.controller;

import org.apache.coyote.BadRequestException;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/user")
    public User signUp(@RequestBody User user) throws Exception {
        return userService.create(user);
    }

    @PatchMapping(value = "/user")
    public void update(@RequestBody User user) throws Exception {
        userService.update(user);
    }

    @DeleteMapping("/admin/user")
    public void delete(@RequestBody User user) throws Exception {
        userService.delete(user);
    }

    @GetMapping(value = "/users")
    public Iterable<User> getAllUsers() {
        return userService.getUsers();
    }

}
