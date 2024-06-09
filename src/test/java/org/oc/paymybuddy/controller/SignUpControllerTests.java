package org.oc.paymybuddy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SpringBootTest
@AutoConfigureMockMvc
public class SignUpControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private MockMvc getMockMvc;
    @Mock
    private UserService userService;

    @InjectMocks
    private SignUpController signUpController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController)
                .build();
    }

    @Test
    public void whenAnonymousAccessSignUp_thenOk() throws Exception {
        getMockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }


    @Test
    public void whenValidSignup_thenRedirectToLogin() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userService.create(user)).thenReturn(user);

        mockMvc.perform(post("/signup")
                        .param("email", user.getEmail())
                        .param("password", user.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).create(user);
    }

    @Test
    public void whenSignupThrowsException_thenRedirectToSignup() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        doThrow(new RuntimeException()).when(userService).create(user);

        mockMvc.perform(post("/signup")
                        .param("email", user.getEmail())
                        .param("password", user.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup"));
    }
}
