package com.qikserve.qikservetest.module.shop.controllers;

import com.qikserve.qikservetest.module.shop.services.ShoppingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/shop")
@Tag(name = "Store - Shopping")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    @PostMapping("/purchase")
    public ResponseEntity<?> shop(@RequestParam String cartToken, @RequestParam String productId, @RequestParam int quantity){
        shoppingService.shop(cartToken, productId, quantity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/finish")
    public ResponseEntity<Boolean> finishShop(
            @RequestParam String cartToken,
            @RequestParam Long paymentMethodId,
            @RequestParam int value) {

        boolean result = shoppingService.finishShop(cartToken, paymentMethodId, value);
        return ResponseEntity.ok(result);
    }


}
