package org.oc.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.oc.paymybuddy.model.BankAccount;
import org.oc.paymybuddy.repository.BankAccountRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountServiceTests {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCreateBankAccount_thenSuccess() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserID(1);

        when(bankAccountRepository.existsByUserID(1)).thenReturn(false);
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount createdBankAccount = bankAccountService.create(bankAccount);

        assertNotNull(createdBankAccount);
        assertEquals(bankAccount, createdBankAccount);
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    public void whenCreateBankAccount_thenThrowException() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserID(1);

        when(bankAccountRepository.existsByUserID(1)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            bankAccountService.create(bankAccount);
        });

        String expectedMessage = "This user already have an account !";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenDeleteBankAccount_thenSuccess() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserID(1);

        when(bankAccountRepository.existsByUserID(1)).thenReturn(true);

        bankAccountService.delete(bankAccount);

        verify(bankAccountRepository, times(1)).delete(bankAccount);
    }

    @Test
    public void whenDeleteNonExistingBankAccount_thenThrowException() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserID(1);

        when(bankAccountRepository.existsByUserID(1)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            bankAccountService.delete(bankAccount);
        });

        String expectedMessage = "This bank account doesn't exist.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenGetBankAccounts_thenSuccess() {
        Iterable<BankAccount> result = bankAccountService.getBankAccounts();

        assertNotNull(result);
        verify(bankAccountRepository, times(1)).findAll();
    }

    @Test
    public void whenGetBankAccountByUserId_thenSuccess() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserID(1);

        when(bankAccountRepository.findBankAccountByUserID(1)).thenReturn(bankAccount);

        BankAccount result = bankAccountService.getBankAccountByUserId(1);

        assertNotNull(result);
        assertEquals(bankAccount, result);
        verify(bankAccountRepository, times(1)).findBankAccountByUserID(1);
    }
}
