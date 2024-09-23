package com.qikserve.qikservetest.module.shop.dtos.responses;

import com.qikserve.qikservetest.module.shop.entities.ProductOrder;

public record ProductOrderResponseDto(Long id, String productId, int quantity, int priceProduct, String promotion) {


    public ProductOrderResponseDto(ProductOrder entity){
        this(entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getPriceProduct(), entity.getPromotion());
    }

}
