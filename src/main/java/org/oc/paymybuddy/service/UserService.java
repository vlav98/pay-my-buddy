package org.oc.paymybuddy.service;

import jakarta.transaction.Transactional;
import org.oc.paymybuddy.constants.Fee;
import org.oc.paymybuddy.exceptions.AlreadyExistingUserException;
import org.oc.paymybuddy.exceptions.InexistentUserException;
import org.oc.paymybuddy.exceptions.NotAuthenticatedException;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public User create(User user) throws AlreadyExistingUserException {
        boolean existingUser = userRepository.existsByEmail(user.getEmail());
        if (existingUser) {
            throw new AlreadyExistingUserException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void delete(User user) throws InexistentUserException {
        boolean existingUser = userRepository.existsByEmail(user.getEmail());
        if (existingUser) {
            userRepository.delete(user);
        } else {
            throw new InexistentUserException("The user you're trying to delete doesn't exist.");
        }
    }

    public void update(User user) throws InexistentUserException {
        User existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            userRepository.save(existingUser);
        } else {
            throw new InexistentUserException("The user you're trying to update doesn't exist.");
        }
    }

    public User getAuthenticatedUser() throws NotAuthenticatedException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (getUserByEmail(username).isEmpty()) {
            throw new NotAuthenticatedException("This email : " + username + " doesn't match any account");
        }

        return getUserByEmail(username).get();
    }

    @Transactional
    public void deposit(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().add(amount.setScale(Fee.SCALE, RoundingMode.HALF_UP)));
        userRepository.save(user);
    }

    @Transactional
    public void withdraw(User user, BigDecimal amount) {
        user.setBalance(user.getBalance().subtract(amount.setScale(Fee.SCALE, RoundingMode.HALF_UP)));
        userRepository.save(user);
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
