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
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void takeDiscountValuesOfTax() {
        RestTemplate restTemplate = new RestTemplate();

        // Pega todas as taxas
        List<Tax> taxes = taxRepository.findAll();

        for (Tax tax : taxes) {
            // Set the header
            HttpHeaders headers = new HttpHeaders();
            headers.set("CategoryOfTax", tax.getName());

            // Creates the request entity
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Makes the request to the Mocky API
            TaxResponseDto response = restTemplate.exchange(taxUrl, HttpMethod.GET, entity, TaxResponseDto.class).getBody();

            if (response != null) {
                //Update the discount percentage of the rate
                tax.setDiscountPercentage(response.discountPercentage());
                taxRepository.save(tax); // Atualiza a taxa no banco
            }
        }
    }

}
