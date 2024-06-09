package org.oc.paymybuddy.service;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.oc.paymybuddy.exceptions.AlreadyExistingUserException;
import org.oc.paymybuddy.exceptions.BuddyNotFoundException;
import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.BeneficiaryRepository;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class BeneficiaryServiceTests {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PaginationService paginationService;

    @InjectMocks
    private BeneficiaryService beneficiaryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void whenAddNewBeneficiary_thenSuccess() throws Exception {
        User sender = new User();
        sender.setUserID(1);
        User recipient = new User();
        recipient.setUserID(2);
        recipient.setEmail("recipient@example.com");

        when(userRepository.findByEmail("recipient@example.com")).thenReturn(Optional.of(recipient));
        when(beneficiaryRepository.existsBySenderAndRecipient(sender.getUserID(), recipient.getUserID())).thenReturn(false);

        beneficiaryService.addNewBeneficiary(sender, "recipient@example.com");
    }

    @Test
    public void whenAddNewBeneficiary_thenThrowBuddyNotFoundException() {
        User sender = new User();
        sender.setUserID(1);

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(BuddyNotFoundException.class, () -> {
            beneficiaryService.addNewBeneficiary(sender, "unknown@example.com");
        });

        String expectedMessage = "Email unknown@example.com does not match any buddy.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenCreateBeneficiary_thenSuccess() throws AlreadyExistingUserException {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setSender(1);
        beneficiary.setRecipient(2);

        when(beneficiaryRepository.existsBySenderAndRecipient(beneficiary.getSender(), beneficiary.getRecipient())).thenReturn(false);
        when(beneficiaryRepository.save(beneficiary)).thenReturn(beneficiary);

        Beneficiary createdBeneficiary = beneficiaryService.create(beneficiary);

        assertNotNull(createdBeneficiary);
        verify(beneficiaryRepository, times(1)).save(beneficiary);
    }

    @Test
    public void whenCreateBeneficiary_thenThrowAlreadyExistingUserException() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setSender(1);
        beneficiary.setRecipient(2);

        when(beneficiaryRepository.existsBySenderAndRecipient(beneficiary.getSender(), beneficiary.getRecipient())).thenReturn(true);

        Exception exception = assertThrows(AlreadyExistingUserException.class, () -> {
            beneficiaryService.create(beneficiary);
        });

        String expectedMessage = "The sender already added the recipient in their friends list.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenDeleteBeneficiary_thenSuccess() throws Exception {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setSender(1);
        beneficiary.setRecipient(2);

        when(beneficiaryRepository.existsBySenderAndRecipient(beneficiary.getSender(), beneficiary.getRecipient())).thenReturn(true);

        beneficiaryService.delete(beneficiary);

        verify(beneficiaryRepository, times(1)).delete(beneficiary);
    }

    @Test
    public void whenDeleteBeneficiary_thenThrowException() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setSender(1);
        beneficiary.setRecipient(2);

        when(beneficiaryRepository.existsBySenderAndRecipient(beneficiary.getSender(), beneficiary.getRecipient())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            beneficiaryService.delete(beneficiary);
        });

        String expectedMessage = "The sender didn't add the recipient in their friends list.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenGetBeneficiaries_thenSuccess() {
        List<Beneficiary> beneficiaries = Arrays.asList(new Beneficiary(), new Beneficiary());
        when(beneficiaryRepository.findAll()).thenReturn(beneficiaries);

        Iterable<Beneficiary> result = beneficiaryService.getBeneficiaries();

        assertNotNull(result);
        assertEquals(2, ((List<Beneficiary>) result).size());
        verify(beneficiaryRepository, times(1)).findAll();
    }

    @Test
    public void whenGetBeneficiariesBySender_thenSuccess() {
        User sender = new User();
        sender.setUserID(1);
        List<Beneficiary> beneficiaries = Arrays.asList(new Beneficiary(), new Beneficiary());
        when(beneficiaryRepository.findBeneficiariesBySender(sender.getUserID())).thenReturn(beneficiaries);

        Iterable<Beneficiary> result = beneficiaryService.getBeneficiariesBySender(sender);

        assertNotNull(result);
        assertEquals(2, ((List<Beneficiary>) result).size());
        verify(beneficiaryRepository, times(1)).findBeneficiariesBySender(sender.getUserID());
    }

    @Test
    public void whenGetBeneficiariesUserBySender_thenSuccess() {
        User sender = new User();
        sender.setUserID(1);
        Beneficiary beneficiary1 = new Beneficiary();
        beneficiary1.setRecipient(2);
        Beneficiary beneficiary2 = new Beneficiary();
        beneficiary2.setRecipient(3);
        List<Beneficiary> beneficiaries = Arrays.asList(beneficiary1, beneficiary2);
        User user1 = new User();
        user1.setUserID(2);
        User user2 = new User();
        user2.setUserID(3);

        when(beneficiaryRepository.findBeneficiariesBySender(sender.getUserID())).thenReturn(beneficiaries);
        when(userRepository.findById(2)).thenReturn(Optional.of(user1));
        when(userRepository.findById(3)).thenReturn(Optional.of(user2));

        List<User> users = beneficiaryService.getBeneficiariesUserBySender(sender);

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(beneficiaryRepository, times(1)).findBeneficiariesBySender(sender.getUserID());
        verify(userRepository, times(1)).findById(2);
        verify(userRepository, times(1)).findById(3);
    }


    @Test
    public void whenBeneficiaryExistsForSenderAndRecipient_thenTrue() {
        User sender = new User();
        sender.setUserID(1);
        User recipient = new User();
        recipient.setUserID(2);
        List<Beneficiary> beneficiaries = Collections.singletonList(new Beneficiary(sender.getUserID(), recipient.getUserID()));

        when(beneficiaryRepository.findBeneficiariesBySender(sender.getUserID())).thenReturn(beneficiaries);

        boolean result = beneficiaryService.getBeneficiaryBySender(sender, recipient);

        assertTrue(result);
        verify(beneficiaryRepository, times(1)).findBeneficiariesBySender(sender.getUserID());
    }

    @Test
    public void whenNoBeneficiaryExistsForSenderAndRecipient_thenFalse() {
        User sender = new User();
        sender.setUserID(1);
        User recipient = new User();
        recipient.setUserID(2);

        when(beneficiaryRepository.findBeneficiariesBySender(sender.getUserID())).thenReturn(Collections.emptyList());

        boolean result = beneficiaryService.getBeneficiaryBySender(sender, recipient);

        assertFalse(result);
        verify(beneficiaryRepository, times(1)).findBeneficiariesBySender(sender.getUserID());
    }

    @Test
    public void whenBeneficiariesExistButNotForSpecifiedRecipient_thenFalse() {
        User sender = new User();
        sender.setUserID(1);
        User recipient = new User();
        recipient.setUserID(2);
        List<Beneficiary> beneficiaries = Arrays.asList(
                new Beneficiary(sender.getUserID(), 3),
                new Beneficiary(sender.getUserID(), 4)
        );

        when(beneficiaryRepository.findBeneficiariesBySender(sender.getUserID())).thenReturn(beneficiaries);

        boolean result = beneficiaryService.getBeneficiaryBySender(sender, recipient);

        assertFalse(result);
        verify(beneficiaryRepository, times(1)).findBeneficiariesBySender(sender.getUserID());
    }
}
