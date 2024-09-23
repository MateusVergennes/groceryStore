package com.qikserve.qikservetest.module.shop.repositories;

import com.qikserve.qikservetest.module.shop.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Orders findByCartTokenAndIsFinishedFalse(String cartToken);

}
