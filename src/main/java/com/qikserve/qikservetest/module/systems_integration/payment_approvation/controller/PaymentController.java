package com.qikserve.qikservetest.module.systems_integration.payment_approvation.controller;

import com.qikserve.qikservetest.module.systems_integration.payment_approvation.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/payment")
@Tag(name = "Payment - Processing")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<Boolean> processPayment(@RequestParam int value) {
        Boolean isApproved = paymentService.processPayment(value);
        return ResponseEntity.status(HttpStatus.OK).body(isApproved);
    }
}