package com.zurnov.bitcoin.insights.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PriceFetcherService {

  private static final RestTemplate restTemplate = new RestTemplate();

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Getter
  private Map<String, Map<String, Double>> cachedPrices = new HashMap<>();

  @Value("${crypto.price.api.url}")
  private String cryptoPriceApiUrl;

  @Value("${crypto.coins}")
  private String[] coins;

  @Value("${crypto.currencies}")
  private String[] currencies;


  @PostConstruct
  public void init() {
    cachedPrices = getCryptoPrice(coins, currencies);
  }

  // It's 300000 (5 minutes) since we are using the free plan
  // for https://www.coingecko.com and we have limited requests 10 000 to be exact
  @Scheduled(fixedRate = 300000)
  private void fetchCryptoPrice() {
    cachedPrices = getCryptoPrice(coins, currencies);
  }

  private Map<String, Map<String, Double>> getCryptoPrice(String[] coins, String[] currencies) {
    String coinIds = String.join(",", coins);
    String currencyIds = String.join(",", currencies);
    String apiUrl = String.format(cryptoPriceApiUrl, coinIds, currencyIds);

    String jsonResponse = restTemplate.getForObject(apiUrl, String.class);
    Map<String, Map<String, Double>> cryptoPrices = new HashMap<>();

    try {
      cryptoPrices = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

    } catch (Exception e) {
      //TODO Better exception handling
      e.printStackTrace();
    }
    return cryptoPrices;
  }
}
