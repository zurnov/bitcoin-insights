package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.config.BitcoinRPCConfig;
import com.zurnov.bitcoin.insights.domain.RPCRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BitcoinRPCClient {

    private final BitcoinRPCConfig config;
    private final RestTemplate restTemplate;

    @Autowired
    public BitcoinRPCClient(BitcoinRPCConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public String sendRPCCommand(String method, List<Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(config.getRpcUser(), config.getRpcPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonRpc = "1.0";

        RPCRequest request = new RPCRequest(jsonRpc,"1", method, params);

        HttpEntity<RPCRequest> entity = new HttpEntity<>(request, headers);

        String url = "http://" + config.getRpcAddress();

        return restTemplate.postForObject(url, entity, String.class);
    }

}
