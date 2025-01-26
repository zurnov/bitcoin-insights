package com.zurnov.bitcoin.insights.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceFetcherService {

  private static final RestTemplate restTemplate = new RestTemplate();

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Getter
  private Double cachedPrice;

  @Value("${crypto.price.api.url}")
  private String cryptoPriceApiUrl;

  @PostConstruct
  public void init() {
    fetchCryptoPrice();
  }

  @Scheduled(fixedRate = 300000)
  private void fetchCryptoPrice() {
    cachedPrice = getCryptoPrice();
  }

  private Double getCryptoPrice() {

    String jsonResponse = restTemplate.getForObject(cryptoPriceApiUrl, String.class);

    try {
      JsonNode rootNode = objectMapper.readTree(jsonResponse);
      JsonNode euroNode = rootNode.path("bpi").path("EUR");
      if (!euroNode.isMissingNode()) {
        return euroNode.path("rate_float").asDouble();
      }
    } catch (Exception e) {
      // Better exception handling can be implemented here
      e.printStackTrace();
    }

    // Return null or a default value in case of an error
    return null;
  }
}
