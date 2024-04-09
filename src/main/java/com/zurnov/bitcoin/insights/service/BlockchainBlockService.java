package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.BlockDTO;
import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainBlockService {

    private final NetworkClientService networkClientService;

    @Autowired
    public BlockchainBlockService(NetworkClientService networkClientService) {
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

    public BlockDTO getBlockInfoByHash(String blockHash) {

        String jsonString = networkClientService.sendRPCCommand("getblock", List.of(blockHash), 8332);

        return createBlockObject(jsonString);
    }

//        public String getBlockchainBlockInfoByBlockHeight(Integer blockHeight) {
//        String jsonString = networkClientService.sendRPCCommand("getblockstats", List.of(blockHeight), 8332);
//        System.out.println(jsonString);
//        return jsonString;
//    }


//TODO Create proper implementation
//    public String getTransactionInfo(String transactionHash) {
//        return networkClientService.sendRPCCommand("getrawtransaction", List.of(transactionHash, true), 8332);
//    }

    private BlockDTO createBlockObject(String jsonString) {

        JSONObject jsonObject = new JSONObject(jsonString);
        List<String> transactions = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("tx");

        for (int i = 0; i < jsonArray.length(); i++) {
            String tx = jsonArray.getString(i);
            transactions.add(tx);
        }

        return BlockDTO.builder()
                .strippedSize(jsonObject.getJSONObject("result").getLong("strippedsize"))
                .size(jsonObject.getJSONObject("result").getLong("size"))
                .weight(jsonObject.getJSONObject("result").getLong("weight"))
                .confirmations(jsonObject.getJSONObject("result").getLong("confirmations"))
                .blockHeight(jsonObject.getJSONObject("result").getLong("height"))
                .version(jsonObject.getJSONObject("result").getLong("version"))
                .nTx(jsonObject.getJSONObject("result").getLong("nTx"))
                .time(jsonObject.getJSONObject("result").getLong("time"))
                .medianTime(jsonObject.getJSONObject("result").getLong("mediantime"))
                .nonce(jsonObject.getJSONObject("result").getLong("nonce"))
                .difficulty(jsonObject.getJSONObject("result").getBigDecimal("difficulty"))
                .hash(jsonObject.getJSONObject("result").getString("hash"))
                .versionHex(jsonObject.getJSONObject("result").getString("versionHex"))
                .merkleRoot(jsonObject.getJSONObject("result").getString("merkleroot"))
                .bits(jsonObject.getJSONObject("result").getString("bits"))
                .chainWork(jsonObject.getJSONObject("result").getString("chainwork"))
                .previousBlockHash(jsonObject.getJSONObject("result").getString("previousblockhash"))
                .nextBlockHash(jsonObject.getJSONObject("result").getString("nextblockhash"))
                .transactions(transactions)
                .build();
    }
}
