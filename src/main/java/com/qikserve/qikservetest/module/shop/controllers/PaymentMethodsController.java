package com.qikserve.qikservetest.module.shop.controllers;

import com.qikserve.qikservetest.module.shop.dtos.requests.PaymentMethodsRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.PaymentMethodsResponseDto;
import com.qikserve.qikservetest.module.shop.services.PaymentMethodsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/payment-methods")
@Tag(name = "Store - CRUD - Payment Methods")
public class PaymentMethodsController {

    @Autowired
    private PaymentMethodsService paymentMethodsService;

    @GetMapping
    public ResponseEntity<List<PaymentMethodsResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(paymentMethodsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodsResponseDto> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentMethodsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<List<PaymentMethodsResponseDto>> save(@RequestBody List<PaymentMethodsRequestDto> paymentMethodsDto) {
        List<PaymentMethodsResponseDto> savedPaymentMethods = paymentMethodsService.save(paymentMethodsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentMethods);
    }

    @PutMapping
    public ResponseEntity<PaymentMethodsResponseDto> update(@RequestBody PaymentMethodsRequestDto paymentMethodDto) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentMethodsService.update(paymentMethodDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        paymentMethodsService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
