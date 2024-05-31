package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.model.User;
import org.oc.paymybuddy.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryService {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

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
}
