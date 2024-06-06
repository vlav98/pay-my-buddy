package org.oc.paymybuddy.service;

import jakarta.transaction.Transactional;
import org.oc.paymybuddy.exceptions.BuddyNotFoundException;
import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.BeneficiaryRepository;
import org.oc.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BeneficiaryService {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Beneficiary addNewBeneficiary(User user, String email) throws Exception {
        Optional<User> beneficiaryToAdd = userRepository.findByEmail(email);
        if (beneficiaryToAdd.isEmpty()) {
            String errorMessage = "Email " + email + " does not match any buddy.";
            throw new BuddyNotFoundException(errorMessage);
        }
        User beneficiary = beneficiaryToAdd.get();
        return create(newBeneficiary(user, beneficiary));
    }

    public Beneficiary newBeneficiary(User user, User beneficiary) {
        Beneficiary beneficiaryLink = new Beneficiary();
        beneficiaryLink.setSender(user.getUserID());
        beneficiaryLink.setRecipient(beneficiary.getUserID());

        return beneficiaryLink;
    }

    public Beneficiary create(Beneficiary beneficiary) throws Exception {
        boolean senderAlreadyHaveRecipient = beneficiaryRepository.existsBySenderAndRecipient(beneficiary.getSender(), beneficiary.getRecipient());
        if (!senderAlreadyHaveRecipient) {
            return beneficiaryRepository.save(beneficiary);
        } else {
            throw new Exception("The sender already added the recipient in their friends list.");
        }
    }

    public void delete(Beneficiary beneficiary) throws Exception {
        boolean senderAlreadyHaveRecipient = beneficiaryRepository.existsBySenderAndRecipient(beneficiary.getSender(), beneficiary.getRecipient());
        if (senderAlreadyHaveRecipient) {
            beneficiaryRepository.delete(beneficiary);
        } else {
            throw new Exception("The sender didn't add the recipient in their friends list.");
        }
    }

    public Iterable<Beneficiary> getBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Iterable<Beneficiary> getBeneficiariesBySender(User sender) {
        return beneficiaryRepository.findBeneficiariesBySender(sender.getUserID());
    }

    public Iterable<User> getBeneficiariesUserBySender(User sender) {
        Iterable<Beneficiary> beneficiaries = beneficiaryRepository.findBeneficiariesBySender(sender.getUserID());
        List<User> usersList = new ArrayList<>();
        for (Beneficiary beneficiary : beneficiaries) {
            Optional<User> user = userRepository.findById(beneficiary.getRecipient());
            user.ifPresent(usersList::add);
        }

        return usersList;
    }

    public boolean getBeneficiaryBySender(User sender, User recipient) {
        Iterable<Beneficiary> beneficiaries = getBeneficiariesBySender(sender);
        boolean hasRecipientId = false;
        for (Beneficiary beneficiary: beneficiaries) {
            if (Objects.equals(beneficiary.getRecipient(), recipient.getUserID())) {
                hasRecipientId = true;
                break;
            }
        }

        return hasRecipientId;
    }
}
