package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.util.AddressUtil;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {


    private final NetworkClientService networkClientService;

    @Autowired
    public AddressServiceImpl(NetworkClientService networkClientService) {
        this.networkClientService = networkClientService;
    }

    public AddressBalanceDTO getAddressBalance(String address) {

        validateRequest(address);

        String addressLookupScriptHash = AddressUtil.generateLookupScriptHash(address);

        AddressBalanceDTO rpcResult = networkClientService.sendRpcTCPRequest(
                "blockchain.scripthash.get_balance",
                List.of(addressLookupScriptHash), 50001);

        return rpcResult;
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
