package com.qikserve.qikservetest.module.shop.dtos.responses;

import com.qikserve.qikservetest.module.shop.entities.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrdersResponseDto(Long id, int totalTaxes, int totalDiscount, int totalPrice, LocalDateTime createdAt, Long paymentMethodId, boolean isFinished, String cartToken, List<ProductOrderResponseDto> productOrders) {

    public OrdersResponseDto(Orders entity){
        this(entity.getId(), entity.getTotalTaxes(), entity.getTotalDiscount(), entity.getTotalPrice(), entity.getCreatedAt(),
                entity.getPaymentMethodId(), entity.isFinished(), entity.getCartToken(),
                entity.getProductOrders().stream().map(ProductOrderResponseDto::new).collect(Collectors.toList()));

    }

}
