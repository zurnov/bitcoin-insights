package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.util.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddressServiceImpl implements AddressService {

    private static final String BITCOIN_ADDRESS_REGEX = "^(bc1|[13])[a-zA-HJ-NP-Z0-9]{25,39}$";


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

        Pattern pattern = Pattern.compile(BITCOIN_ADDRESS_REGEX);
        Matcher matcher = pattern.matcher(address);

        if (!matcher.matches()) {
            throw new ValidationException("Invalid Bitcoin address format!");
        }
    }
}
