package com.qikserve.qikservetest.module.shop.services;

import com.qikserve.qikservetest.module.shop.dtos.requests.ProductOrderRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.ProductOrderResponseDto;
import com.qikserve.qikservetest.module.shop.entities.ProductOrder;
import com.qikserve.qikservetest.module.shop.repositories.ProductOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Transactional
    public List<ProductOrderResponseDto> findAll() {
        return productOrderRepository.findAll().stream().map(ProductOrderResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public ProductOrderResponseDto findById(Long id){
        var entity = productOrderRepository.findById(id);
        return entity.map(ProductOrderResponseDto::new).orElse(null);
    }

    private ProductOrder convertToCartItems(ProductOrderRequestDto dto){
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(dto.id());
        productOrder.setProductId(dto.productId());
        productOrder.setQuantity(dto.quantity());
        productOrder.setPriceProduct(dto.priceProduct());
        productOrder.setPromotion(dto.promotion());
        return productOrder;
    }

    public List<ProductOrderResponseDto> save(List<ProductOrderRequestDto> dataList) {
        List<ProductOrder> savedCartItems = dataList.stream()
                .map(this::convertToCartItems)
                .map(productOrderRepository::save)
                .toList();

        return savedCartItems.stream()
                .map(ProductOrderResponseDto::new)
                .collect(Collectors.toList());
    }

    public ProductOrderResponseDto update(ProductOrderRequestDto cartItem){
        var entity = productOrderRepository.findById(cartItem.id()).orElse(null);
        if (entity != null){
            entity.setProductId(cartItem.productId());
            entity.setQuantity(cartItem.quantity());
            entity.setPriceProduct(cartItem.priceProduct());
            entity.setPromotion(cartItem.promotion());
            entity = productOrderRepository.save(entity);
        }
        return entity != null ? new ProductOrderResponseDto(entity) : null;
    }

    public void deleteById(Long id){
        productOrderRepository.deleteById(id);
    }

}
