package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.entity.PriceHistory;
import com.zurnov.bitcoin.insights.repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BitcoinPriceService {

  private final PriceFetcherService priceFetcherService;
  private final PriceHistoryRepository priceHistoryRepository;

  @Autowired
  public BitcoinPriceService(PriceFetcherService priceFetcherService, PriceHistoryRepository priceHistoryRepository) {
    this.priceFetcherService = priceFetcherService;
    this.priceHistoryRepository = priceHistoryRepository;
  }

  public Double getBitcoinPriceInUsd() {
   return priceFetcherService.getCachedPrice();
  }

  @Scheduled(cron = "0 0 * * * ?")
  public void fetchAndSaveBitcoinPrice() {
    Double btcPrice = getBitcoinPriceInUsd();
    savePriceToDatabase(btcPrice);
  }


  private void savePriceToDatabase(Double price) {
    if (price != null) {
      PriceHistory priceHistory = new PriceHistory();
      priceHistory.setTimestamp(LocalDateTime.now());
      priceHistory.setPriceUsd(price);
      priceHistoryRepository.save(priceHistory);
    }
  }
}
