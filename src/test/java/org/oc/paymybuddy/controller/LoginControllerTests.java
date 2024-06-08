package org.oc.paymybuddy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private MockMvc getMockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousAccessLogin_thenOk() throws Exception {
        getMockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenValidCredentials_thenOk() throws Exception {
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("user", "password");
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = new UsernamePasswordAuthenticationToken(loginRequest.username(), null, null);

        when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidCredentials_thenUnauthorized() throws Exception {
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("user", "wrongpassword");
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        when(authenticationManager.authenticate(authenticationRequest)).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void whenAuthenticationFails_thenInternalServerError() throws Exception {
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("user", "password");
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        when(authenticationManager.authenticate(authenticationRequest)).thenThrow(new AuthenticationException("Authentication failed") {});

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isInternalServerError());
    }
}
