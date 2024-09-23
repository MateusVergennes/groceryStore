package com.qikserve.qikservetest.module.shop.repositories;

import com.qikserve.qikservetest.module.shop.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
