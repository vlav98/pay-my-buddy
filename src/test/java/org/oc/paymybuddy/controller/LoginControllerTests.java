package org.oc.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenAnonymousAccessLogin_thenOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAnonymousAccessRestrictedEndpoint_thenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/all"))
                .andExpect(status().isUnauthorized());
    }

}
