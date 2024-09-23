package com.qikserve.qikservetest.module.systems_integration.mocky_api.services;

import com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos.ProductDetailDto;
import com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockyApiService {

    @Value("${mocky.api.url.products}")
    private String productsUrl;

    public List<ProductDto> getAllProducts() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ProductDto[]> response = restTemplate.exchange(productsUrl, HttpMethod.GET, entity, ProductDto[].class);

        // Retorna a lista de produtos
        return List.of(response.getBody());
    }

    public ProductDetailDto getProductById(String id) {
        ProductDetailDto productDetail = null; // Variável para armazenar o resultado

        RestTemplate restTemplate = new RestTemplate();
        String url = productsUrl + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProductDetailDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ProductDetailDto.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                productDetail = response.getBody(); // Define o resultado
            }
        } catch (Exception e) {
            // Captura qualquer exceção e mantém productDetail como null
        }

        return productDetail; // Retorna null se não encontrado ou erro
    }

    public List<ProductDetailDto> getProductsByIds(List<String> ids) {
        List<ProductDetailDto> products = new ArrayList<>();

        for (String id : ids) {
            ProductDetailDto product = getProductById(id);
            if (product != null) {
                products.add(product);
            }
        }

        return products;
    }

}
