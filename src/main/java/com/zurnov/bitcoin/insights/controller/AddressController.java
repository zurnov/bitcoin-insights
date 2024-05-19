package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.dto.AddressTransactionHistoryDTO;
import com.zurnov.bitcoin.insights.service.AddressService;
import com.zurnov.bitcoin.insights.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @GetMapping("/getaddressbalance/{address}")
    public ResponseEntity<AddressBalanceDTO> getAddressBalance(@PathVariable String address) {

        String requestId = RequestUtil.generateRequestId();
        log.info(">> Received new request for getAddressBalance ID: {}", requestId);
        AddressBalanceDTO rpcResult = addressService.getAddressBalance(address, requestId);
        log.info("<< getAddressBalance ID: {}\n", requestId);
        return new ResponseEntity<>(rpcResult,HttpStatus.OK);
    }

    @GetMapping("/getaddresshistory/{address}")
    public ResponseEntity<AddressTransactionHistoryDTO> getAddressHistory(
            @PathVariable String address,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        String requestId = RequestUtil.generateRequestId();
        log.info(">> Received new request for getAddressHistory ID: {}", requestId);
        AddressTransactionHistoryDTO json = addressService.getAddressTransactionHistory(address, pageNumber, pageSize, requestId);
        log.info("<< getAddressHistory ID: {}\n", requestId);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
