package com.qikserve.qikservetest.module.shop.dtos.requests;

public record ProductOrderRequestDto(Long id, String productId, int quantity, int priceProduct, String promotion) {
}
