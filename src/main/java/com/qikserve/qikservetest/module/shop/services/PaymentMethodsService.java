package com.qikserve.qikservetest.module.shop.services;

import com.qikserve.qikservetest.module.shop.dtos.requests.PaymentMethodsRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.PaymentMethodsResponseDto;
import com.qikserve.qikservetest.module.shop.entities.PaymentMethods;
import com.qikserve.qikservetest.module.shop.repositories.PaymentMethodsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodsService {

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;

    @Transactional
    public List<PaymentMethodsResponseDto> findAll() {
        return paymentMethodsRepository.findAll().stream()
                .map(PaymentMethodsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentMethodsResponseDto findById(Long id) {
        var entity = paymentMethodsRepository.findById(id);
        return entity.map(PaymentMethodsResponseDto::new).orElse(null);
    }

    private PaymentMethods convertToEntity(PaymentMethodsRequestDto dto) {
        PaymentMethods paymentMethod = new PaymentMethods();
        paymentMethod.setId(dto.id());
        paymentMethod.setName(dto.name());
        paymentMethod.setNeedAuthorization(dto.needAuthorization());
        return paymentMethod;
    }

    public List<PaymentMethodsResponseDto> save(List<PaymentMethodsRequestDto> dataList) {
        List<PaymentMethods> savedPaymentMethods = dataList.stream()
                .map(this::convertToEntity)
                .map(paymentMethodsRepository::save)
                .collect(Collectors.toList());

        return savedPaymentMethods.stream()
                .map(PaymentMethodsResponseDto::new)
                .collect(Collectors.toList());
    }

    public PaymentMethodsResponseDto update(PaymentMethodsRequestDto paymentMethodDto) {
        var entity = paymentMethodsRepository.findById(paymentMethodDto.id()).orElse(null);
        if (entity != null) {
            entity.setName(paymentMethodDto.name());
            entity.setNeedAuthorization(paymentMethodDto.needAuthorization());
            entity = paymentMethodsRepository.save(entity);
        }
        return entity != null ? new PaymentMethodsResponseDto(entity) : null;
    }

    public void deleteById(Long id) {
        paymentMethodsRepository.deleteById(id);
    }

}
