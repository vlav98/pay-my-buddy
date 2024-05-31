package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) throws Exception {
        boolean existingUser = userRepository.existsByEmail(user.getEmail());
        if (existingUser) {
            throw new Exception("The user already exists with this email.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void delete(User user) throws Exception {
        boolean existingUser = userRepository.existsByEmailAndFirstName(user.getEmail(), user.getFirstName());
        if (existingUser) {
            userRepository.delete(user);
        } else {
            throw new Exception("The user you're trying to delete doesn't exist.");
        }
    }

    public void update(User user) throws Exception {
        User existingUser = userRepository.findUserByEmailAndFirstName(user.getEmail(), user.getFirstName());
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            userRepository.save(existingUser);
        } else {
            throw new Exception("The user you're trying to update doesn't exist.");
        }
    }

    public User getAuthenticatedUser() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (getUserByEmail(username).isEmpty()) {
            throw new Exception("This email : " + username + " doesn't match any account");
        }

        return getUserByEmail(username).get();
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
