package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;

public interface AddressService {

    AddressBalanceDTO getAddressBalance(String address);

}
