package org.oc.paymybuddy.repository;


import org.oc.paymybuddy.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Integer> {
    Iterable<Beneficiary> findBeneficiariesBySender(Integer sender);

    boolean existsBySenderAndRecipient(Integer sender, Integer recipient);

    Beneficiary getBeneficiaryBySenderAndRecipient(Integer sender, Integer recipient);
}
