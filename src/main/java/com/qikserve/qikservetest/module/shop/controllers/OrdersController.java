package com.qikserve.qikservetest.module.shop.controllers;

import com.qikserve.qikservetest.module.shop.dtos.requests.OrdersRequestoDto;
import com.qikserve.qikservetest.module.shop.dtos.requests.ProductOrderRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.OrdersResponseDto;
import com.qikserve.qikservetest.module.shop.services.OrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/orders")
@Tag(name = "Store - CRUD - Orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public ResponseEntity<List<OrdersResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersResponseDto> findById(@PathVariable(name = "id") Long id) {
        OrdersResponseDto orderResponse = ordersService.findById(id);
        return orderResponse != null
                ? ResponseEntity.status(HttpStatus.OK).body(orderResponse)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/cart-token/{token}")
    public ResponseEntity<OrdersResponseDto> getOrderByCartTokenAndNotFinished(@PathVariable String token) {
        OrdersResponseDto order = ordersService.findByCartTokenAndNotFinished(token);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<List<OrdersResponseDto>> save(@RequestBody List<OrdersRequestoDto> ordersDto) {
        List<OrdersResponseDto> savedOrders = ordersService.save(ordersDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrders);
    }

    @PutMapping
    public ResponseEntity<OrdersResponseDto> update(@RequestBody OrdersRequestoDto orderDto) {
        OrdersResponseDto updatedOrder = ordersService.update(orderDto);
        return updatedOrder != null
                ? ResponseEntity.status(HttpStatus.OK).body(updatedOrder)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{orderId}/add-product")
    public ResponseEntity<OrdersResponseDto> addProductToOrder(
            @PathVariable Long orderId,
            @RequestBody ProductOrderRequestDto productOrderDto) {
        OrdersResponseDto responseDto = ordersService.addProductToOrder(orderId, productOrderDto);
        ResponseEntity<OrdersResponseDto> response;

        if (responseDto != null) {
            response = ResponseEntity.ok(responseDto);
        } else {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        ordersService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
