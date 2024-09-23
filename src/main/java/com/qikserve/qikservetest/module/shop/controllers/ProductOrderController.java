package com.qikserve.qikservetest.module.shop.controllers;

import com.qikserve.qikservetest.module.shop.dtos.requests.ProductOrderRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.ProductOrderResponseDto;
import com.qikserve.qikservetest.module.shop.services.ProductOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/product-order")
@Tag(name = "Store - CRUD - Product of Order")
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @GetMapping
    public ResponseEntity<List<ProductOrderResponseDto>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(productOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderResponseDto> findById(@PathVariable(name = "id")Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productOrderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<List<ProductOrderResponseDto>> save(@RequestBody List<ProductOrderRequestDto> cartItemsDto){
        List<ProductOrderResponseDto> savedCartItems = productOrderService.save(cartItemsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCartItems);
    }

    @PutMapping
    public ResponseEntity<ProductOrderResponseDto> update(@RequestBody ProductOrderRequestDto cartItemsDto){
        return ResponseEntity.status(HttpStatus.OK).body(productOrderService.update(cartItemsDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id")Long id){
        productOrderService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
