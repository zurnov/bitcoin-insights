package com.zurnov.bitcoin.insights.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.dto.AddressTransactionHistoryDTO;
import com.zurnov.bitcoin.insights.dto.TransactionDTO;
import com.zurnov.bitcoin.insights.exception.OperationFailedException;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.util.AddressUtil;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {


    private final NetworkClientService networkClientService;

    private final ObjectMapper objectMapper;

    @Autowired
    public AddressServiceImpl(NetworkClientService networkClientService, ObjectMapper objectMapper) {
        this.networkClientService = networkClientService;
        this.objectMapper = objectMapper;
    }

    public AddressBalanceDTO getAddressBalance(String address, String requestId) {

        log.info("  >> getAddressBalance ID: {}", requestId);

        validateRequest(address, requestId);

        String addressLookupScriptHash = AddressUtil.generateLookupScriptHash(address);

        String jsonString = networkClientService.sendRpcTCPRequest(
                "blockchain.scripthash.get_balance",
                List.of(addressLookupScriptHash), 50001, requestId);

        JSONObject json = new JSONObject(jsonString);

        Long confirmedInteger = json.getJSONObject("result").getLong("confirmed");
        Long unconfirmedInteger = json.getJSONObject("result").getLong("unconfirmed");

        log.info("  << getAddressBalance ID: {}", requestId);
        return new AddressBalanceDTO(confirmedInteger, unconfirmedInteger);

    }

    public AddressTransactionHistoryDTO getAddressTransactionHistory(String address, Integer pageNumber, Integer pageSize, String requestId){

        log.info("  >> getAddressTransactionHistory ID: {}", requestId);

        validateRequest(address, requestId);

        String addressLookupScriptHash = AddressUtil.generateLookupScriptHash(address);

        String jsonString = networkClientService.sendRpcTCPRequest(
                "blockchain.scripthash.get_history",
                List.of(addressLookupScriptHash), 50001, requestId);

        jsonString = jsonString.replace("result", "transactions");
        jsonString = jsonString.replace("height", "blockHeight");
        jsonString = jsonString.replace("tx_hash", "txHash");

        try {

            AddressTransactionHistoryDTO addressTransactionHistoryDTO  =
                    objectMapper.readValue(jsonString, AddressTransactionHistoryDTO.class);

            int totalPages = calculatePagination(addressTransactionHistoryDTO.getTransactions().size(), pageSize, pageNumber);
            List<TransactionDTO> allTransactions = addressTransactionHistoryDTO.getTransactions();
            Collections.reverse(allTransactions);

            List<TransactionDTO> transactions =
                allTransactions
                    .stream()
                    .skip((pageNumber - 1) * pageSize)
                    .limit(pageSize).toList();
            addressTransactionHistoryDTO.setTransactions(transactions);
            addressTransactionHistoryDTO.setTotalPages(totalPages);

            log.info("  << getAddressTransactionHistory ID: {}", requestId);

            return addressTransactionHistoryDTO;
        } catch (Exception e) {
            throw new OperationFailedException(e.getMessage());
        }
    }

    private int calculatePagination(int listSize, int pageSize, int pageNumber) {
        int totalPages = listSize / pageSize;
        if (listSize % pageSize != 0) {
            totalPages++;
        }

        if (pageNumber < 1 || pageNumber > totalPages) {
            throw new ValidationException("Invalid page number. Please provide a page number between 1 and " + totalPages);
        }

        return totalPages;
    }

    private void validateRequest(String address, String requestId) {

        log.info("      >> validateRequest ID: {}", requestId);
        if (address == null || address.isEmpty()) {
            throw new ValidationException("Address must not be empty or null!");
        }

        NetworkParameters params = MainNetParams.get();

        try {
            Address.fromString(params, address);
        } catch (Exception e) {
            throw new ValidationException("Invalid Address format!");
        }
        log.info("      << validateRequest ID: {}", requestId);
    }

}
