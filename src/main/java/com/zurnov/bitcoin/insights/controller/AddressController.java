package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.domain.Address;
import com.zurnov.bitcoin.insights.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AddressController {

    AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(path = "/address/{address}")
    public Mono<Address> getAddress(@PathVariable String address) {

        return addressService.getAddressInfo(address);
    }

    @GetMapping(path = "/hello/{name}")
    public String getHello(@PathVariable String name) {

        return String.format("Hello %s!", name);
    }
}
