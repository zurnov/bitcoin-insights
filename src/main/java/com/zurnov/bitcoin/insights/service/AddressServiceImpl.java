package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.Address;
import com.zurnov.bitcoin.insights.dto.AddressBalanceDTO;
import com.zurnov.bitcoin.insights.exception.ValidationException;
import com.zurnov.bitcoin.insights.util.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddressServiceImpl implements AddressService {

    private static final String BITCOIN_ADDRESS_REGEX = "^(bc1|[13])[a-zA-HJ-NP-Z0-9]{25,39}$";

    private final WebClient localApiClient;
    private final String secretToken;

    private final NetworkClientService networkClientService;

    @Autowired
    public AddressServiceImpl(WebClient localApiClient, @Value("${secret.token}") String secretToken, NetworkClientService networkClientService) {
        this.localApiClient = localApiClient;
        this.secretToken = secretToken;
        this.networkClientService = networkClientService;
    }

    @Override
    public Mono<Address> getAddressInfo(String address) {

        return localApiClient.get()
                .uri("/addrs/{address}?token={token}", address, secretToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new Throwable("Bad Request"))).bodyToMono(Address.class);
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
