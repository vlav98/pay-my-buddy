package org.oc.paymybuddy.controller;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.service.BeneficiaryService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BeneficiaryService beneficiaryService;

    @InjectMocks
    private HomeController homeController;


    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void testShowHomePage() throws Exception {
        User mockUser = new User();
        List<User> mockBeneficiaries = new ArrayList<>();

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(beneficiaryService.getBeneficiariesUserBySender(mockUser)).thenReturn(mockBeneficiaries);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("page", "home"))
                .andExpect(model().attribute("beneficiaries", mockBeneficiaries));
    }
}
