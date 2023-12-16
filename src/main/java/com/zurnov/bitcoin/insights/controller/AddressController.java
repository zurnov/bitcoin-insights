package com.zurnov.bitcoin.insights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurnov.bitcoin.insights.domain.Address;
import com.zurnov.bitcoin.insights.domain.JsonResponse;
import com.zurnov.bitcoin.insights.service.AddressService;
import com.zurnov.bitcoin.insights.service.BitcoinRPCClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
public class AddressController {

    AddressService addressService;
    BitcoinRPCClient rpcClient;

    @Autowired
    public AddressController(AddressService addressService, BitcoinRPCClient rpcClient) {
        this.addressService = addressService;
        this.rpcClient = rpcClient;
    }

    @GetMapping(path = "/address/{address}")
    public Mono<Address> getAddress(@PathVariable String address) {

        return addressService.getAddressInfo(address);
    }

    @GetMapping(path = "/hello/{name}")
    public String getHello(@PathVariable String name) {

        return String.format("Hello %s!", name);
    }

    @GetMapping("/getblockchaininfo")
    public ResponseEntity<Object> getBlockchainInfo() {
//        String rpcResult = rpcClient.sendRPCCommand("listtransactions", List.of("1LnoZawVFFQihU8d8ntxLMpYheZUfyeVAK"));
        String rpcResult = rpcClient.sendRPCCommand("getindexinfo", new ArrayList<>());
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonResponse jsonResponse = mapper.readValue(rpcResult, JsonResponse.class);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }
}
