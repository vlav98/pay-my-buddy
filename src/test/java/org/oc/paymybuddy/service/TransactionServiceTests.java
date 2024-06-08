package org.oc.paymybuddy.service;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.paymybuddy.constants.Fee;
import org.oc.paymybuddy.exceptions.InsufficientBalanceException;
import org.oc.paymybuddy.model.BankAccount;
import org.oc.paymybuddy.model.Transaction;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.model.dto.AmountAndFee;
import org.oc.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceTests {


    @MockBean
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;
    @MockBean
    private BeneficiaryService beneficiaryService;
    @Autowired
    private PaginationService paginationService;
    @MockBean
    private BankAccountService bankAccountService;

    private static final User sender = new User();
    private static final User recipient = new User();
    private static final Transaction mockTransaction = new Transaction();
    private static final BankAccount mockBankAccount = new BankAccount();

    private static final String description = "Transaction Test";
    private static final double amount = 20.0;

    @BeforeAll
    public static void setUp() {
        sender.setUserID(1);
        sender.setFirstName("Test 1");
        sender.setEmail("test@email.com");
        sender.setBalance(BigDecimal.valueOf(200));

        recipient.setFirstName("Test 2");
        recipient.setEmail("admin@mail.com");
        recipient.setBalance(BigDecimal.valueOf(10));

        mockTransaction.setDescription(description);
        mockTransaction.setBankAccountID(1);
        mockTransaction.setAmount(BigDecimal.valueOf(amount));
        mockTransaction.setSenderID(sender.getUserID());
        mockTransaction.setRecipient(recipient.getEmail());

        mockBankAccount.setUserID(1);
        mockBankAccount.setBankAccountID(1);
    }

    @Test
    public void whenUserCreateTransactionWithNegativeAmount_thenThrow() {
        // GIVEN
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> transactionService.create(sender,
                recipient, "failed transaction", 0));
    }

    @Test
    public void whenUserHasNotEnoughBalance_thenThrow() {
        // GIVEN
        // WHEN
        // THEN
        assertThrows(InsufficientBalanceException.class, () -> transactionService.create(sender,
                recipient, "failed transaction", 500));
    }

    @Test
    public void whenUserSelfSendsMoney_thenThrow() {
        // GIVEN
        // WHEN
        // THEN
        assertThrows(Exception.class, () -> transactionService.create(sender,
                sender, "failed transaction", 20));
    }

    @Test
    public void whenUserHasNotRecipientAsBeneficiary_thenThrow() {
        // GIVEN
        // WHEN
        Mockito.when(beneficiaryService.getBeneficiaryBySender(sender, recipient)).thenReturn(false);
        // THEN
        assertThrows(Exception.class, () -> transactionService.create(sender,
                recipient, "failed transaction", 20));
    }

    @Test
    public void whenUserHasRecipientAsBeneficiary_thenIsOk() throws Exception {
        // GIVEN
        // WHEN
        Mockito.when(beneficiaryService.getBeneficiaryBySender(sender, recipient)).thenReturn(true);
        Mockito.when(bankAccountService.getBankAccountByUserId(sender.getUserID())).thenReturn(mockBankAccount);
        Mockito.when(transactionRepository.save(mockTransaction)).thenReturn(mockTransaction);
        // THEN

        Transaction savedTransaction = transactionService.create(sender, recipient, description, amount);

        assertNotNull(savedTransaction);
        assertEquals(description, savedTransaction.getDescription());
        assertEquals(BigDecimal.valueOf(amount), savedTransaction.getAmount());
        assertEquals(sender.getUserID(), savedTransaction.getSenderID());
        assertEquals(recipient.getEmail(), savedTransaction.getRecipient());
    }

    @Test
    public void whenCalculateAmountWithFee() throws Exception {
        // GIVEN
        BigDecimal fees = new BigDecimal(Double.toString(amount * Fee.TRANSACTION_FEE)).setScale(Fee.SCALE, RoundingMode.HALF_UP);
        // WHEN
        AmountAndFee amountAndFee = transactionService.calculateAmountWithFee(amount);
        // THEN
        assertNotNull(amountAndFee);
        assertEquals(BigDecimal.valueOf(amount).setScale(Fee.SCALE, RoundingMode.HALF_UP), amountAndFee.getAmount());
        assertEquals(fees, amountAndFee.getFee());
        assertEquals(amountAndFee.getAmount().add(fees), amountAndFee.getAmountWithFee());
    }
}
