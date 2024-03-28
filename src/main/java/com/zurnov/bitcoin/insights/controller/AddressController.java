package com.zurnov.bitcoin.insights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurnov.bitcoin.insights.domain.JsonResponse;
import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.dto.AddressTransactionHistoryDTO;
import com.zurnov.bitcoin.insights.service.AddressService;
import com.zurnov.bitcoin.insights.service.NetworkClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class AddressController {

    private final AddressService addressService;
    private final NetworkClientService networkClientService;

    private final ObjectMapper mapper;

    @Autowired
    public AddressController(AddressService addressService, NetworkClientService networkClientService, ObjectMapper mapper) {
        this.addressService = addressService;
        this.networkClientService = networkClientService;
        this.mapper = mapper;
    }

    @GetMapping("/getblockchaininfo")
    public ResponseEntity<Object> getBlockchainInfo() {
        String rpcResult = networkClientService.sendRPCCommand("getindexinfo", new ArrayList<>(), 8332);
        try {
            JsonResponse jsonResponse = mapper.readValue(rpcResult, JsonResponse.class);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/getaddressbalance/{address}")
    public ResponseEntity<AddressBalanceDTO> getAddressBalance(@PathVariable String address) {

        AddressBalanceDTO rpcResult = addressService.getAddressBalance(address);
        return new ResponseEntity<>(rpcResult,HttpStatus.OK);
    }

    @GetMapping("/getaddresshistory/{address}")
    public ResponseEntity<AddressTransactionHistoryDTO> getAddressHistory(
            @PathVariable String address,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        AddressTransactionHistoryDTO json = addressService.getAddressHistory(address, pageNumber, pageSize);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
