package com.qikserve.qikservetest.module.systems_integration.tax.schedulejob;

import com.qikserve.qikservetest.module.systems_integration.tax.repositories.TaxRepository;
import com.qikserve.qikservetest.module.systems_integration.tax.dtos.response.TaxResponseDto;
import com.qikserve.qikservetest.module.systems_integration.tax.entities.Tax;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@EnableScheduling
@Service
public class TaxDiscountValueService {

    @Autowired
    private TaxRepository taxRepository;

    @Value("${tax.url}")
    private String taxUrl;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // Executa todos os dias à meia-noite
    public void takeDiscountValuesOfTax() {
        RestTemplate restTemplate = new RestTemplate();

        // Pega todas as taxas
        List<Tax> taxes = taxRepository.findAll();

        for (Tax tax : taxes) {
            // Configura o cabeçalho
            HttpHeaders headers = new HttpHeaders();
            headers.set("CategoryOfTax", tax.getName());

            // Cria a entidade de requisição
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Faz a requisição para a API Mocky
            TaxResponseDto response = restTemplate.exchange(taxUrl, HttpMethod.GET, entity, TaxResponseDto.class).getBody();

            if (response != null) {
                // Atualiza o discountPercentage da taxa
                tax.setDiscountPercentage(response.discountPercentage());
                taxRepository.save(tax); // Atualiza a taxa no banco
            }
        }
    }

}
