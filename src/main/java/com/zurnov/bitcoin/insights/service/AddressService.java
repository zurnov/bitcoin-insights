package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.Address;
import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import reactor.core.publisher.Mono;

public interface AddressService {

    Mono<Address> getAddressInfo(String address);

    AddressBalanceDTO getAddressBalance(String address);

}
