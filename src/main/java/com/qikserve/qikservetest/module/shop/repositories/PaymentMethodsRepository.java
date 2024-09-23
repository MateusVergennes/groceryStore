package com.qikserve.qikservetest.module.shop.repositories;

import com.qikserve.qikservetest.module.shop.entities.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Long> {
}
