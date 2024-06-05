package org.oc.paymybuddy.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    private static final User user = new User();

    @BeforeAll
    public static void setUp() {
        user.setFirstName("Sender User");
        user.setEmail("sender@test.com");
        user.setPassword("SenderPwd");
    }

    @Test
    public void canNotCreateExistingUser() {
        // GIVEN
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> userService.create(user));
    }

    @Test
    public void canCreateUser() throws Exception {
        // GIVEN
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        // WHEN
        User userCreated = userService.create(user);
        // THEN
        assertThat(userCreated).isNotNull();
        assertThat(userCreated).isEqualTo(user);
    }

    @Test
    public void canNotDeleteNonExistingUser() {
        // GIVEN
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> userService.delete(user));
    }

    @Test
    public void canDeleteUser() throws Exception {
        // GIVEN
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        Mockito.doNothing().when(userRepository).delete(user);
        // WHEN
        userService.delete(user);
        // THEN
        Mockito.verify(userRepository).delete(user);
    }

    @Test
    public void canNotUpdateUser() throws Exception {
        // GIVEN
        Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(null);
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> userService.update(user));
    }

    @Test
    public void canUpdateUser() throws Exception {
        // GIVEN
        Mockito.when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);
        // WHEN
        userService.update(user);
        // THEN
        Mockito.verify(userRepository).save(user);
    }
}
