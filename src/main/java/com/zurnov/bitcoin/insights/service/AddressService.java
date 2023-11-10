package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.Address;
import reactor.core.publisher.Mono;

public interface AddressService {

    Mono<Address> getAddressInfo(String address);
}
