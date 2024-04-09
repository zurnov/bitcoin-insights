package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDTO {

    private long strippedSize;
    private long size;
    private long weight;
    private long confirmations;
    private long blockHeight;
    private long version;
    private long nTx;
    private long time;
    private long medianTime;
    private long nonce;

    private BigDecimal difficulty;

    private String hash;
    private String versionHex;
    private String merkleRoot;
    private String bits;
    private String chainWork;
    private String previousBlockHash;
    private String nextBlockHash;
    private List<String> transactions;

}
