package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.dto.AddressTransactionHistoryDTO;

public interface AddressService {

    AddressBalanceDTO getAddressBalance(String address);

    AddressTransactionHistoryDTO getAddressTransactionHistory(String address, Integer pageNumber, Integer pageSize);

}
