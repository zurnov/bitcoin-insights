package com.zurnov.bitcoin.insights.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReference {

    @JsonProperty("tx_hash")
    private String transactionHash;

    @JsonProperty("block_height")
    private Integer blockHeight;

    @JsonProperty("value")
    private BigInteger value;

    @JsonProperty("ref_balance")
    private BigInteger referenceBalance;

    @JsonProperty("false")
    private boolean isSpent;

    @JsonProperty("confirmations")
    private Integer confirmations;

    @JsonProperty("confirmed")
    private LocalDateTime confirmedAt;

    @JsonProperty("double_spend")
    private boolean isDoubleSpend;

}
