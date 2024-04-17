package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RawTransactionInfoDTO {

    private String txId;
    private String hash;
    private Integer size;
    private Integer vBytes;
    private Integer weight;
    private Integer version;
    private Long lockTime;
    private List<Vin> vin;
    private List<Vout> vout;
    private String hex;
    private String blockHash;
    private Integer confirmations;
    private Long blockTime;
    private Long time;

}
