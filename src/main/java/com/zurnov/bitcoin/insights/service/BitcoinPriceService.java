package com.zurnov.bitcoin.insights.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BitcoinPriceService {

  private final PriceFetcherService priceFetcherService;

  @Autowired
  public BitcoinPriceService(PriceFetcherService priceFetcherService) {
    this.priceFetcherService = priceFetcherService;
  }

  public Double getBitcoinPriceInUsd() {
    Map<String, Map<String, Double>> latestPrices = priceFetcherService.getCachedPrices();

    Double btcPrice = null;

    if (latestPrices.containsKey("bitcoin")) {
      Map<String, Double> bitcoinPrices = latestPrices.get("bitcoin");
      btcPrice = bitcoinPrices.get("usd");
    }

    return btcPrice;
  }
}
