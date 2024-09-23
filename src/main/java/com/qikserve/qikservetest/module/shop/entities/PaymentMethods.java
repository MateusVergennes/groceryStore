package com.qikserve.qikservetest.module.shop.entities;

import com.qikserve.qikservetest.module.shop.dtos.requests.PaymentMethodsRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean needAuthorization;

    public PaymentMethods(PaymentMethodsRequestDto data){
        this.id = data.id();
        this.name = data.name();
        this.needAuthorization = data.needAuthorization();
    }

}
