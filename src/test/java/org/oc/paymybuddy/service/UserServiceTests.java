package org.oc.paymybuddy.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.oc.paymybuddy.constants.Fee;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;
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
        verify(userRepository).delete(user);
    }

    @Test
    public void canNotUpdateUser() {
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
        verify(userRepository).save(user);
    }



    @Test
    public void whenDeposit_thenBalanceIncreased() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100.00));
        BigDecimal depositAmount = BigDecimal.valueOf(50.00);

        userService.deposit(user, depositAmount);

        BigDecimal expectedBalance = BigDecimal.valueOf(150.00).setScale(Fee.SCALE, RoundingMode.HALF_UP);
        assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenWithdraw_thenBalanceDecreased() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100.00));
        BigDecimal withdrawalAmount = BigDecimal.valueOf(50.00);

        userService.withdraw(user, withdrawalAmount);

        BigDecimal expectedBalance = BigDecimal.valueOf(50.00).setScale(Fee.SCALE, RoundingMode.HALF_UP);
        assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenWithdrawMoreThanBalance_thenBalanceNegative() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100.00));
        BigDecimal withdrawalAmount = BigDecimal.valueOf(150.00);

        userService.withdraw(user, withdrawalAmount);

        BigDecimal expectedBalance = BigDecimal.valueOf(-50.00).setScale(Fee.SCALE, RoundingMode.HALF_UP);
        assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).save(user);
    }
}
