package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
}
