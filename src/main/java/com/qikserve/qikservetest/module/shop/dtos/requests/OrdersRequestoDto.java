package com.qikserve.qikservetest.module.shop.dtos.requests;

import java.util.List;

public record OrdersRequestoDto(Long id, int totalTaxes, int totalDiscount, int totalPrice, Long paymentMethodId, boolean isFinished, String cartToken, List<ProductOrderRequestDto> productOrders) {
}
