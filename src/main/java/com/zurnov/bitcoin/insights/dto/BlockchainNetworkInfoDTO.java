package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainNetworkInfoDTO {

    private Long blocks;
    private BigDecimal difficulty;
    private BigDecimal networkHashPerSecond;
    private Long pooledTx;
    private String chain;

}
