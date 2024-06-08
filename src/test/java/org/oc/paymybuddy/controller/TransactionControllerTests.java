package org.oc.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.model.dto.AmountAndFee;
import org.oc.paymybuddy.model.viewModel.TransactionFormViewModel;
import org.oc.paymybuddy.service.BeneficiaryService;
import org.oc.paymybuddy.service.TransactionService;
import org.oc.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {

    @MockBean
    private UserService userService;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private BeneficiaryService beneficiaryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    private static final User sender = new User();
    private static final Transaction transaction = new Transaction();
    private static final Beneficiary beneficiary = new Beneficiary();
    private static final String validEmail = "admin@mail.com";

    Transaction mockTransaction = Mockito.mock(Transaction.class);

    @BeforeAll
    public static void setUsers() {
        sender.setEmail("test@email.com");
        sender.setBalance(new BigDecimal(200));

        User recipient = new User();
        recipient.setEmail(validEmail);
    }

    @BeforeAll
    public static void setTransaction() {
        transaction.setBankAccountID(1);
        transaction.setSenderID(1);
        transaction.setRecipient(validEmail);
        transaction.setDescription("Add transaction test");
        transaction.setAmount(BigDecimal.valueOf(10.0));
    }

    @BeforeAll
    public static void setBeneficiary() {
        beneficiary.setRecipient(3);
        beneficiary.setSender(1);
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousAccessTransactionPage_thenIsUnauthorized() throws Exception {
        assertThrows(Exception.class, () -> mockMvc.perform(get("/transaction")));
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousAddBeneficiary_thenIsForbidden() throws Exception {
        mockMvc.perform(post("/transaction/add-beneficiary"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAddBeneficiary_thenIsOk() throws Exception {
        mockMvc.perform(post("/transaction/add-beneficiary")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(transaction))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserShowAddBeneficiary_thenThrows() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(get("/transaction/add-beneficiary")
                        .with(csrf())
                        .param("email", "invalidEmail@mail.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "anon", roles = {"GUEST"})
    public void whenAnonymousAddTransaction_thenIsForbidden() throws Exception {
        mockMvc.perform(post("/transaction/pay"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAddsTransaction_withInvalidRecipient_thenError() throws Exception {
        mockMvc.perform(post("/transaction/pay").with(csrf())
                        .param("action", "payment")
                        .content(objectMapper.writeValueAsString(mockTransaction))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAccessPayPage_thenIsOk() throws Exception {
        mockMvc.perform(get("/transaction/pay").with(csrf())
                        .param("action", "payment")
                        .content(objectMapper.writeValueAsString(mockTransaction))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("payment"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("transactionForm"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAddTransactionRedirect_thenIsOk() throws Exception {
        // GIVEN
        User mockRecipientUser = Mockito.mock(User.class);
        TransactionFormViewModel mockTransactionForm = Mockito.mock(TransactionFormViewModel.class);
        BigDecimal amount = BigDecimal.valueOf(10.00);
        BigDecimal fee = BigDecimal.valueOf(0.05);
        BigDecimal amountWithFees = BigDecimal.valueOf(10.05);
        AmountAndFee mockAmountAndFee = new AmountAndFee(amount, fee, amountWithFees);
        // WHEN

        when(mockTransactionForm.getRecipient()).thenReturn(validEmail);
        when(transactionService.calculateAmountWithFee(anyDouble())).thenReturn(mockAmountAndFee);

        mockMvc.perform(post("/transaction/pay")
                        .with(csrf())
                        .param("action", "redirect")
                        .param("recipient", mockTransactionForm.getRecipient())
                        .param("amount", String.valueOf(mockTransactionForm.getAmount()))
                        .param("description", mockTransactionForm.getDescription())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(view().name("/payment"))
                .andExpect(model().attributeExists("transactionForm"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void whenUserAddTransaction_thenIsOk() throws Exception {
        // WHEN
        User mockRecipientUser = Mockito.mock(User.class);
        TransactionFormViewModel mockTransactionForm = Mockito.mock(TransactionFormViewModel.class);

        when(mockTransactionForm.getRecipient()).thenReturn(validEmail);
        when(userService.getUserByEmail(validEmail)).thenReturn(Optional.of(mockRecipientUser));
        when(transactionService.create(any(), any(), anyString(), anyDouble())).thenReturn(null);

        mockMvc.perform(post("/transaction/pay")
                        .with(csrf())
                        .param("action", "payment")
                        .param("recipient", mockTransactionForm.getRecipient())
                        .param("amount", String.valueOf(mockTransactionForm.getAmount()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("success"));
    }

}
