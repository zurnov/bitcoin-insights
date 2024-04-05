package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class BlockchainNetworkService {

    private final NetworkClientService networkClientService;

    @Autowired
    public BlockchainNetworkService(NetworkClientService networkClientService) {
        this.networkClientService = networkClientService;
    }

    public BlockchainNetworkInfoDTO getBlockchainNetworkInfo() {

        BlockchainNetworkInfoDTO blockchainNetworkInfoDTO;

        String jsonString = networkClientService.sendRPCCommand("getmininginfo", new ArrayList<>(), 8332);

        JSONObject jsonObject = new JSONObject(jsonString);

        Long blocks = jsonObject.getJSONObject("result").getLong("blocks");
        BigDecimal difficulty = jsonObject.getJSONObject("result").getBigDecimal("difficulty");
        BigDecimal networkHashPerSecond = jsonObject.getJSONObject("result").getBigDecimal("networkhashps");
        Long pooledTx = jsonObject.getJSONObject("result").getLong("pooledtx");
        String chain = jsonObject.getJSONObject("result").getString("chain");

        blockchainNetworkInfoDTO = BlockchainNetworkInfoDTO.builder()
                .blocks(blocks)
                .difficulty(difficulty)
                .networkHashPerSecond(networkHashPerSecond)
                .pooledTx(pooledTx)
                .chain(chain)
                .build();

        return blockchainNetworkInfoDTO;
    }
}
