package com.zurnov.bitcoin.insights.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @JsonProperty("address")
    private String walletAddress;

    @JsonProperty("total_received")
    private BigInteger totalReceived;

    @JsonProperty("total_sent")
    private BigInteger totalSent;

    @JsonProperty("balance")
    private BigInteger balance;

    @JsonProperty("unconfirmed_balance")
    private BigInteger unconfirmedBalance;

    @JsonProperty("final_balance")
    private BigInteger finalBalance;

    @JsonProperty("n_tx")
    private BigInteger numberOfTransactions;

    @JsonProperty("unconfirmed_n_tx")
    private BigInteger numberOfUnconfirmedTransactions;

    @JsonProperty("final_n_tx")
    private BigInteger finalNumberOfTransactions;

}
