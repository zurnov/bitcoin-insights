package com.zurnov.bitcoin.insights.service;

import com.zurnov.bitcoin.insights.dto.BlockDTO;
import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import com.zurnov.bitcoin.insights.dto.RawTransactionInfoDTO;
import com.zurnov.bitcoin.insights.dto.ScriptPubKey;
import com.zurnov.bitcoin.insights.dto.ScriptSig;
import com.zurnov.bitcoin.insights.dto.Vin;
import com.zurnov.bitcoin.insights.dto.Vout;
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

    public BlockDTO getBlockInfoByHeight(Integer blockHeight) {

        String hashString = networkClientService.sendRPCCommand("getblockhash", List.of(blockHeight), 8332);
        JSONObject jsonObject = new JSONObject(hashString);

        String blockHash = jsonObject.getString("result");
        String jsonString = networkClientService.sendRPCCommand("getblock", List.of(blockHash), 8332);


        return createBlockObject(jsonString);
    }


    public RawTransactionInfoDTO getRawTransactionInfo(String transactionHash) {

        String jsonString = networkClientService.sendRPCCommand("getrawtransaction", List.of(transactionHash, true), 8332);

        return createTransactionInfoObject(jsonString);
    }


    private static RawTransactionInfoDTO createTransactionInfoObject(String jsonString) {
        
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject resultObject = jsonObject.getJSONObject("result");

        return mapRawTransactionInfoFields(resultObject);
    }

    private static RawTransactionInfoDTO mapRawTransactionInfoFields(JSONObject resultObject) {

        RawTransactionInfoDTO rawTransactionInfoDTO = mapSimpleRawTransactionInfoFields(resultObject);

        // Map Vin
        JSONArray vinArray = resultObject.getJSONArray("vin");
        List<Vin> vinList = mapVinFields(vinArray);
        rawTransactionInfoDTO.setVin(vinList);

        // Map Vout
        List<Vout> voutList = mapVoutFields(resultObject);
        rawTransactionInfoDTO.setVout(voutList);
        return rawTransactionInfoDTO;
    }

    private static RawTransactionInfoDTO mapSimpleRawTransactionInfoFields(JSONObject resultObject) {

        RawTransactionInfoDTO rawTransactionInfoDTO = new RawTransactionInfoDTO();

        rawTransactionInfoDTO.setHex(resultObject.getString("hex"));
        rawTransactionInfoDTO.setTxId(resultObject.getString("txid"));
        rawTransactionInfoDTO.setHash(resultObject.getString("hash"));
        rawTransactionInfoDTO.setSize(resultObject.getInt("size"));
        rawTransactionInfoDTO.setVBytes(resultObject.getInt("vsize"));
        rawTransactionInfoDTO.setWeight(resultObject.getInt("weight"));
        rawTransactionInfoDTO.setVersion(resultObject.getInt("version"));
        rawTransactionInfoDTO.setLockTime(resultObject.getLong("locktime"));
        rawTransactionInfoDTO.setBlockHash(resultObject.getString("blockhash"));
        rawTransactionInfoDTO.setConfirmations(resultObject.getInt("confirmations"));
        rawTransactionInfoDTO.setBlockTime(resultObject.getLong("blocktime"));
        rawTransactionInfoDTO.setTime(resultObject.getLong("time"));

        return rawTransactionInfoDTO;
    }

    private static List<Vout> mapVoutFields(JSONObject resultObject) {

        JSONArray voutArray = resultObject.getJSONArray("vout");
        List<Vout> voutList = new ArrayList<>();
        for (int i = 0; i < voutArray.length(); i++) {
            JSONObject voutObject = voutArray.getJSONObject(i);
            Vout vout = new Vout();
            vout.setValue(voutObject.getDouble("value"));
            vout.setN(voutObject.getInt("n"));

            // Map ScriptPubKey
            JSONObject scriptPubKeyObject = voutObject.getJSONObject("scriptPubKey");
            ScriptPubKey scriptPubKey = new ScriptPubKey();
            scriptPubKey.setAsm(scriptPubKeyObject.getString("asm"));
            scriptPubKey.setHex(scriptPubKeyObject.getString("hex"));
            scriptPubKey.setType(scriptPubKeyObject.getString("type"));
            scriptPubKey.setAddress(scriptPubKeyObject.optString("address"));
            vout.setScriptPubKey(scriptPubKey);

            voutList.add(vout);
        }

        return voutList;
    }

    private static List<Vin> mapVinFields(JSONArray vinArray) {

        List<Vin> vinList = new ArrayList<>();
        for (int i = 0; i < vinArray.length(); i++) {
            JSONObject vinObject = vinArray.getJSONObject(i);
            Vin vin = new Vin();
            vin.setTxId(vinObject.optString("txid"));
            vin.setVout(vinObject.optInt("vout"));
            vin.setCoinbase(vinObject.optString("coinbase"));
            vin.setSequence(vinObject.getLong("sequence"));

            // Map ScriptSig
            JSONObject scriptSigObject = vinObject.optJSONObject("scriptSig");
            if (scriptSigObject != null) {
                ScriptSig scriptSig = new ScriptSig();
                scriptSig.setAsm(scriptSigObject.getString("asm"));
                scriptSig.setHex(scriptSigObject.getString("hex"));
                vin.setScriptSig(scriptSig);
            }


            // Map txInWitness
            JSONArray txInWitnessArray = vinObject.optJSONArray("txinwitness");
            if (txInWitnessArray != null) {
                List<String> txInWitnessList = new ArrayList<>();
                for (int j = 0; j < txInWitnessArray.length(); j++) {
                    txInWitnessList.add(txInWitnessArray.getString(j));
                }
                vin.setTxInWitness(txInWitnessList);
            }

            vinList.add(vin);
        }

        return vinList;
    }

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
