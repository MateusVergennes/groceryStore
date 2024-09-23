package com.qikserve.qikservetest.module.systems_integration.mocky_api.controller;

import com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos.ProductDetailDto;
import com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos.ProductDto;
import com.qikserve.qikservetest.module.systems_integration.mocky_api.services.MockyApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/mocky")
@Tag(name = "Mocky API")
public class MockApiController {

    @Autowired
    private MockyApiService mockyApiService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = mockyApiService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDetailDto> getProductById(@PathVariable String id) {
        ProductDetailDto product = mockyApiService.getProductById(id);
        return product != null
                ? ResponseEntity.status(HttpStatus.OK).body(product)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
