package com.zurnov.bitcoin.insights.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.dto.AddressTransactionHistoryDTO;
import org.json.JSONObject;

public interface AddressService {

    AddressBalanceDTO getAddressBalance(String address);

    AddressTransactionHistoryDTO getAddressHistory(String address);

}
