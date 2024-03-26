package com.zurnov.bitcoin.insights.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurnov.bitcoin.insights.config.BitcoinRPCConfig;
import com.zurnov.bitcoin.insights.domain.RPCRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NetworkClient {

    private final BitcoinRPCConfig config;
    private final RestTemplate restTemplate;

    @Autowired
    public NetworkClient(BitcoinRPCConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public String sendRPCCommand(String method, List<Object> params, int port) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(config.getRpcUser(), config.getRpcPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonRpc = "2.0";

        RPCRequest request = new RPCRequest(jsonRpc,"zurnov", method, params);

        HttpEntity<RPCRequest> entity = new HttpEntity<>(request, headers);

        String url = "http://" + config.getRpcAddress() + ":" + port;

        return restTemplate.postForObject(url, entity, String.class);
    }

public String sendRpcTCPRequest(String method, List<Object> params, int port) {
    try {

        Socket socket = new Socket(config.getRpcAddress(), port);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id", "zurnov");
        requestMap.put("method", method);
        requestMap.put("params", params);

        String jsonRequest = mapper.writeValueAsString(requestMap);

        out.write(jsonRequest);
        out.newLine();
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String jsonResponse = in.readLine();

        socket.close();

        return jsonResponse;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


}
