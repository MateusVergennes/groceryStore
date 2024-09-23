package com.qikserve.qikservetest.module.shop.entities;

import com.qikserve.qikservetest.module.shop.dtos.requests.ProductOrderRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private int quantity;
    private int priceProduct;
    private String promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    public ProductOrder(ProductOrderRequestDto data){
        this.id = data.id();
        this.productId = data.productId();
        this.quantity = data.quantity();
        this.priceProduct = data.priceProduct();
        this.promotion = data.promotion();
    }

}
