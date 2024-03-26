package com.zurnov.bitcoin.insights.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurnov.bitcoin.insights.domain.Address;
import com.zurnov.bitcoin.insights.domain.JsonResponse;
import com.zurnov.bitcoin.insights.service.AddressService;
import com.zurnov.bitcoin.insights.service.NetworkClient;
import com.zurnov.bitcoin.insights.util.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AddressController {

    AddressService addressService;
    NetworkClient rpcClient;

    @Autowired
    public AddressController(AddressService addressService, NetworkClient rpcClient) {
        this.addressService = addressService;
        this.rpcClient = rpcClient;
    }

    @GetMapping(path = "/address/{address}")
    public Mono<Address> getAddress(@PathVariable String address) {

        return addressService.getAddressInfo(address);
    }

    @GetMapping("/getblockchaininfo")
    public ResponseEntity<Object> getBlockchainInfo() {
        String rpcResult = rpcClient.sendRPCCommand("getindexinfo", new ArrayList<>(), 8332);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonResponse jsonResponse = mapper.readValue(rpcResult, JsonResponse.class);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/getaddressbalance/{address}")
    public ResponseEntity<Object> getAddressBalance(@PathVariable String address) {
        String addressLookupScriptHash = AddressUtil.generateLookupScriptHash(address);
        String rpcResult = rpcClient.sendRpcTCPRequest("blockchain.scripthash.get_balance", List.of(addressLookupScriptHash), 50001);
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
