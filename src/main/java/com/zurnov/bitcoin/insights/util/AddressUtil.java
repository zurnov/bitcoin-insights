package com.zurnov.bitcoin.insights.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.security.MessageDigest;

@UtilityClass
public class AddressUtil {

    public static String generateLookupScriptHash(String address) {

        NetworkParameters params = MainNetParams.get();
        Address bitcoinAddress = Address.fromString(params, address);
        Script script = ScriptBuilder.createOutputScript(bitcoinAddress);

        byte[] reversedHash = Utils.reverseBytes(sha256(script.getProgram()));

        return Hex.encodeHexString(reversedHash);
    }

    private static byte[] sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
