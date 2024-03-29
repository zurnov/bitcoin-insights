package com.zurnov.bitcoin.insights.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.dto.AddressTransactionHistoryDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.util.AddressUtil;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {


    private final NetworkClientService networkClientService;

    private final ObjectMapper objectMapper;

    @Autowired
    public AddressServiceImpl(NetworkClientService networkClientService, ObjectMapper objectMapper) {
        this.networkClientService = networkClientService;
        this.objectMapper = objectMapper;
    }

    public AddressBalanceDTO getAddressBalance(String address) {

        validateRequest(address);

        String addressLookupScriptHash = AddressUtil.generateLookupScriptHash(address);

        String jsonString = networkClientService.sendRpcTCPRequest(
                "blockchain.scripthash.get_balance",
                List.of(addressLookupScriptHash), 50001);

        JSONObject json = new JSONObject(jsonString);

        Long confirmedInteger = json.getJSONObject("result").getLong("confirmed");
        Long unconfirmedInteger = json.getJSONObject("result").getLong("unconfirmed");

        return new AddressBalanceDTO(confirmedInteger, unconfirmedInteger);

    }

    public AddressTransactionHistoryDTO getAddressHistory(String address){

        validateRequest(address);

        String addressLookupScriptHash = AddressUtil.generateLookupScriptHash(address);

        String jsonString = networkClientService.sendRpcTCPRequest(
                "blockchain.scripthash.get_history",
                List.of(addressLookupScriptHash), 50001);

        jsonString = jsonString.replace("result", "transactions");
        jsonString = jsonString.replace("height", "blockHeight");
        jsonString = jsonString.replace("tx_hash", "txHash");

        try {
            return objectMapper.readValue(jsonString, AddressTransactionHistoryDTO.class);
        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage());
        }
    }

    private void validateRequest(String address) {

        if (address == null || address.isEmpty()) {
            throw new ValidationException("Address must not be empty or null!");
        }

        NetworkParameters params = MainNetParams.get();

        try {
            Address.fromString(params, address);
        } catch (Exception e) {
            throw new ValidationException("Invalid Address format!");
        }

    }

}
