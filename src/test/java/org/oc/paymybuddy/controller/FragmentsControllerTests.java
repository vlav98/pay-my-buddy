package org.oc.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.model.dto.ContactMessage;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;


@SpringBootTest
@AutoConfigureMockMvc
public class FragmentsControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private MockMvc getMockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private FragmentsController fragmentsController;
    private static final ContactMessage contactMessage = new ContactMessage();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fragmentsController)
                .build();
    }

    @BeforeAll
    public static void setContactMessage() {
        contactMessage.setObject("Message Object");
        contactMessage.setMessageText("Hello, this is a test message. Hope you have a nice day !");
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousAccessProfile_thenIsUnauthorized() throws Exception {
        assertThrows(Exception.class, () -> mockMvc.perform(get("/profile")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn());
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAccessProfile_thenIsOk() throws Exception {
        getMockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousUpdateBalance_thenIsUnauthorized() throws Exception {
        assertThrows(Exception.class, () -> mockMvc.perform(get("/profile/update-balance")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn());
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserUpdateBalance_thenIsOk() throws Exception {
        // GIVEN
        User mockUser = new User();
        mockUser.setEmail("test@email.com");

        // WHEN
        when(userService.getAuthenticatedUser()).thenReturn(mockUser);

        // THEN
        getMockMvc.perform(get("/profile/update-balance"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-balance"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenValidDeposit_thenRedirectToProfileWithSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@email.com");
        BigDecimal amount = new BigDecimal("100.00");

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);

        mockMvc.perform(post("/profile/update-balance")
                        .param("action", "deposit")
                        .param("amount", amount.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("success", "Your deposit of 100.00€ was successful!"));

        verify(userService).deposit(mockUser, amount);
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenValidWithdrawal_thenRedirectToProfileWithSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@email.com");
        BigDecimal amount = new BigDecimal("50.00");

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);

        mockMvc.perform(post("/profile/update-balance")
                        .param("action", "withdrawal")
                        .param("amount", amount.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("success", "Your withdrawal of 50.00€ was successful!"));

        verify(userService).withdraw(mockUser, amount);
    }


    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenExceptionDuringUpdate_thenRedirectToUpdateBalanceWithError() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@email.com");
        BigDecimal amount = new BigDecimal("50.00");

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        doThrow(new RuntimeException("Insufficient funds")).when(userService).withdraw(mockUser, amount);

        mockMvc.perform(post("/profile/update-balance")
                        .param("action", "withdrawal")
                        .param("amount", amount.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/update-balance"))
                .andExpect(flash().attribute("error", "Insufficient funds"));
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousAccessContact_thenIsOk() throws Exception {
        getMockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserSendMessage_thenIsOk() throws Exception {
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(contactMessage))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", "Your message has been received!"));
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousSendMessage_thenIsOk() throws Exception {
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(contactMessage))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.flash().attribute("successMessage", "Your message has been received!"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAccessContact_thenIsOk() throws Exception {
        getMockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAnonymousAccessRestrictedEndpoint_thenIsUnauthorized() throws Exception {
        getMockMvc.perform(get("/all"))
                .andExpect(status().isUnauthorized());
    }

}
