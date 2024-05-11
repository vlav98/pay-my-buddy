package org.oc.paymybuddy.service;

import org.oc.paymybuddy.model.Beneficiary;
import org.oc.paymybuddy.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryService {
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    public Beneficiary create(Beneficiary beneficiary) {
        return beneficiaryRepository.save(beneficiary);
    }

    public void delete(Beneficiary beneficiary) {
        beneficiaryRepository.delete(beneficiary);
    }

    public Iterable<Beneficiary> getBeneficiaries() {
        return beneficiaryRepository.findAll();
    }
}
