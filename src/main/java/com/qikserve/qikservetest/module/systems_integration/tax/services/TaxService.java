package com.qikserve.qikservetest.module.systems_integration.tax.services;

import com.qikserve.qikservetest.module.systems_integration.tax.repositories.TaxRepository;
import com.qikserve.qikservetest.module.systems_integration.tax.entities.Tax;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaxService {

    @Autowired
    private TaxRepository taxRepository;

    @Transactional
    public List<Tax> findAll() {
        return taxRepository.findAll();
    }

    @Transactional
    public Tax findById(Long id) {
        return taxRepository.findById(id).orElse(null);
    }

    @Transactional
    public Optional<Tax> findByName(String name) { // Adiciona este método
        return taxRepository.findByName(name);
    }

    @Transactional
    public Tax save(Tax tax) {
        return taxRepository.save(tax);
    }

    @Transactional
    public void updateTax(Tax tax) {
        taxRepository.save(tax);
    }

    @Transactional
    public void deleteById(Long id) {
        taxRepository.deleteById(id);
    }

}
