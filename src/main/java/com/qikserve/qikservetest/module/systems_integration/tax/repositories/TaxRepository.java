package com.qikserve.qikservetest.module.systems_integration.tax.repositories;

import com.qikserve.qikservetest.module.systems_integration.tax.entities.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxRepository extends JpaRepository<Tax, Long> {

    Optional<Tax> findByName(String name);

}
