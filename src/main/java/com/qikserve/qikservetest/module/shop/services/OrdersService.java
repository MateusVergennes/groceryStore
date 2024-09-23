package com.qikserve.qikservetest.module.shop.services;

import com.qikserve.qikservetest.module.shop.dtos.requests.OrdersRequestoDto;
import com.qikserve.qikservetest.module.shop.dtos.requests.ProductOrderRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.OrdersResponseDto;
import com.qikserve.qikservetest.module.shop.entities.Orders;
import com.qikserve.qikservetest.module.shop.entities.ProductOrder;
import com.qikserve.qikservetest.module.shop.repositories.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductOrderService productOrderService;

    @Transactional
    public List<OrdersResponseDto> findAll() {
        return ordersRepository.findAll().stream().map(OrdersResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public OrdersResponseDto findById(Long id) {
        var entity = ordersRepository.findById(id);
        return entity.map(OrdersResponseDto::new).orElse(null);
    }

    @Transactional
    public OrdersResponseDto findByCartTokenAndNotFinished(String token) {
        OrdersResponseDto responseDto;

        Orders order = ordersRepository.findByCartTokenAndIsFinishedFalse(token);

        if (order != null) {
            responseDto = new OrdersResponseDto(order);
        } else {
            responseDto = null;
        }
        return responseDto;
    }

    private Orders convertToOrder(OrdersRequestoDto dto) {
        Orders orders = new Orders();
        orders.setId(dto.id());
        orders.setTotalTaxes(dto.totalTaxes());
        orders.setTotalDiscount(dto.totalDiscount());
        orders.setTotalPrice(dto.totalPrice());
        orders.setCreatedAt(LocalDateTime.now());
        orders.setPaymentMethodId(dto.paymentMethodId());
        orders.setFinished(dto.isFinished());
        orders.setCartToken(dto.cartToken());

        // Mapeia os ProductOrderRequestDto para ProductOrder e os associa ao Order
        List<ProductOrder> productOrders = dto.productOrders().stream()
                .map(productOrderDto -> {
                    ProductOrder productOrder = new ProductOrder(productOrderDto);
                    productOrder.setOrders(orders);
                    return productOrder;
                })
                .collect(Collectors.toList());

        orders.setProductOrders(productOrders);
        return orders;
    }

    public List<OrdersResponseDto> save(List<OrdersRequestoDto> dataList) {
        List<Orders> savedOrders = dataList.stream()
                .map(this::convertToOrder)
                .map(ordersRepository::save)
                .collect(Collectors.toList());

        return savedOrders.stream()
                .map(OrdersResponseDto::new)
                .collect(Collectors.toList());
    }

    public OrdersResponseDto update(OrdersRequestoDto orderDto) {
        var entity = ordersRepository.findById(orderDto.id()).orElse(null);
        if (entity != null) {
            entity.setTotalTaxes(orderDto.totalTaxes());
            entity.setTotalDiscount(orderDto.totalDiscount());
            entity.setTotalPrice(orderDto.totalPrice());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setPaymentMethodId(orderDto.paymentMethodId());
            entity.setFinished(orderDto.isFinished());
            entity.setCartToken(orderDto.cartToken());

            for (var productOrderDto : orderDto.productOrders()) {
                productOrderService.update(productOrderDto);
            }

            entity = ordersRepository.save(entity);
        }
        return entity != null ? new OrdersResponseDto(entity) : null;
    }

    @Transactional
    public OrdersResponseDto addProductToOrder(Long orderId, ProductOrderRequestDto productOrderDto) {
        OrdersResponseDto responseDto = null;

        var orderOptional = ordersRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Orders existingOrder = orderOptional.get();
            List<ProductOrder> existingProductOrders = existingOrder.getProductOrders();

            // Checks if the product is already in the order's ProductOrders list
            ProductOrder existingProductOrder = existingProductOrders.stream()
                    .filter(po -> po.getProductId().equals(productOrderDto.productId()))
                    .findFirst()
                    .orElse(null);

            if (existingProductOrder != null) {
                existingProductOrder.setQuantity(existingProductOrder.getQuantity() + productOrderDto.quantity());
            } else {
                ProductOrder newProductOrder = new ProductOrder(productOrderDto);
                newProductOrder.setOrders(existingOrder);
                existingOrder.getProductOrders().add(newProductOrder);
            }

            Orders updatedOrder = ordersRepository.save(existingOrder);

            responseDto = new OrdersResponseDto(updatedOrder);
        }

        return responseDto;
    }

    public void deleteById(Long id) {
        ordersRepository.deleteById(id);
    }

}
