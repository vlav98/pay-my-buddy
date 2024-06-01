package org.oc.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.willThrow;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.oc.paymybuddy.security.CustomUserDetails;
import org.oc.paymybuddy.service.CustomUserDetailsService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    private static final User sender = new User();
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeAll
    public static void setUser() {
        sender.setFirstName("Sender User");
        sender.setEmail("sender@test.com");
        sender.setPassword("SenderPwd");
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void signUpNewUser() throws Exception {
        // GIVEN
        Mockito.when(userService.create(sender)).thenReturn(sender);
        // WHEN
        MvcResult mvcResult = mockMvc.perform(post("/user")
                .with(csrf())
                .content(objectMapper.writeValueAsString(sender))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isCreated()).andReturn();

        Mockito.verify(userService).create(sender);
        String resultUser = mvcResult.getResponse().getContentAsString();

        objectMapper.readValue(
                resultUser, User.class
        );
        Assertions.assertThat(resultUser).isNotNull();
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void canNotSignUpExistingUser() throws Exception {
        // GIVEN
        Exception exception = new Exception("The user already exists with this email.");
        Mockito.when(userService.create(sender)).thenThrow(exception);
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> mockMvc.perform(post("/user")
                .with(csrf())
                .content(objectMapper.writeValueAsString(sender))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn());
    }


    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void canNotUpdateExistingUser() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(patch("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void canUpdateExistingUser() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(patch("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void canNotUpdateNonExistingUser() throws Exception {
        // GIVEN
        Exception exception = new Exception("The user you're trying to update doesn't exist.");
        doThrow(exception).when(userService).update(sender);
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> mockMvc.perform(patch("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()));
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void canNotDeleteExistingUser() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(delete("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void canDeleteExistingUser() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(delete("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void canNotDeleteNonExistingUser() throws Exception {
        // GIVEN
        Exception exception = new Exception("The user you're trying to delete doesn't exist.");
        doThrow(exception).when(userService).delete(sender);
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> mockMvc.perform(delete("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()));
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void canNotGetAllUsersInformationWhenGuest() throws Exception {
        mockMvc.perform(get("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "anon", roles = {"MEMBER"})
    public void canNotGetAllUsersInformationWhenMember() throws Exception {
        mockMvc.perform(get("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "anon", roles = {"ADMIN"})
    public void canGetAllUsersInformationWhenAdmin() throws Exception {
        mockMvc.perform(get("/user")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(sender))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
