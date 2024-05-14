package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public User create(User user) throws Exception {
        boolean existingUser = userRepository.existsByEmail(user.getEmail());
        if (existingUser) {
            throw new Exception("The user already exists with this email.");
        }
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

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmailAndFirstName(String email, String firstName) {
        return userRepository.findUserByEmailAndFirstName(email, firstName);
    }

    /**
     * @param email
     * @return userDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }
}
