package com.qikserve.qikservetest.module.shop.entities;

import com.qikserve.qikservetest.module.shop.dtos.requests.OrdersRequestoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalTaxes;
    private int totalDiscount;
    private int totalPrice;
    private LocalDateTime createdAt;
    private Long paymentMethodId;
    private boolean isFinished;
    private String cartToken;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductOrder> productOrders = new ArrayList<>();

    public Orders(OrdersRequestoDto data){
        this.id = data.id();
        this.totalTaxes = data.totalTaxes();
        this.totalDiscount = data.totalDiscount();
        this.totalPrice = data.totalPrice();
        this.createdAt = LocalDateTime.now();
        this.paymentMethodId = data.paymentMethodId();
        this.isFinished = data.isFinished();
        this.cartToken = data.cartToken();
    }

}
