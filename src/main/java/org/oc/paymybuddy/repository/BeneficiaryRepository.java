package org.oc.paymybuddy.repository;


import org.oc.paymybuddy.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Integer> {
}
