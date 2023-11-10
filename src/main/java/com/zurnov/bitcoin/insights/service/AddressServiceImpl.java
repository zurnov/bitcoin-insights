package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AddressServiceImpl implements AddressService{


    private final WebClient localApiClient;
    private final String secretToken;

    @Autowired
    public AddressServiceImpl(WebClient localApiClient, @Value("${secret.token}") String secretToken) {
        this.localApiClient = localApiClient;
        this.secretToken = secretToken;
    }

    @Override
    public Mono<Address> getAddressInfo(String address) {

        return localApiClient.get()
                .uri("/addrs/{address}?token={token}", address, secretToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new Throwable("Bad Request"))).bodyToMono(Address.class);
    }
}
