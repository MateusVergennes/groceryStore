package com.qikserve.qikservetest.module.systems_integration.payment_approvation.service;

import com.qikserve.qikservetest.module.systems_integration.payment_approvation.dtos.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class PaymentService {

    @Value("${payment.url}")
    private String paymentUrl;

    @Value("${payment.token}")
    private String paymentToken;


    private String getPaymentStatus(int value) {
        RestTemplate restTemplate = new RestTemplate();

        // Configurando headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Adicionando Authorization no formato Base64
        String encodedToken = Base64.getEncoder().encodeToString(("Bearer " + paymentToken).getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", encodedToken);

        // Criando o corpo da requisição
        String requestBody = "{\"amount\":" + value + "}";

        // Realizando a chamada POST
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<PaymentResponseDto> response = restTemplate.exchange(paymentUrl, HttpMethod.POST, entity, PaymentResponseDto.class);

        // Retornando o status do pagamento
        return response.getBody() != null ? response.getBody().statusPayment() : null;
    }

    public Boolean processPayment(int value) {
        String status = getPaymentStatus(value);
        return "Approved".equals(status);
    }
}