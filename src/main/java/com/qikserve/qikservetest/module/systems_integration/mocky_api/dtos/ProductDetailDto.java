package com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos;

import java.util.List;

public record ProductDetailDto(String id, String name, int price, List<PromotionDto> promotions) {
}
