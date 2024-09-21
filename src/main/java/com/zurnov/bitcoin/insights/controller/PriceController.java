package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.service.BitcoinPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/price")
public class PriceController {

  private final BitcoinPriceService bitcoinPriceService;

  @Autowired
  public PriceController(BitcoinPriceService bitcoinPriceService) {
    this.bitcoinPriceService = bitcoinPriceService;
  }

  @GetMapping("/btc")
  public ResponseEntity<Double> getBitcoinPrice() {
      return new ResponseEntity<>(bitcoinPriceService.getBitcoinPriceInUsd(), HttpStatus.OK);
  }
}
