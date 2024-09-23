package com.qikserve.qikservetest.module.shop.dtos.responses;

import com.qikserve.qikservetest.module.shop.entities.PaymentMethods;

public record PaymentMethodsResponseDto(Long id, String name, boolean needAuthorization) {

    public PaymentMethodsResponseDto(PaymentMethods entity){
        this(entity.getId(), entity.getName(), entity.isNeedAuthorization());
    }

}
