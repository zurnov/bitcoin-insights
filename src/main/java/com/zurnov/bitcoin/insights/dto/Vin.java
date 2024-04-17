package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vin {
    private String txId;
    private String coinbase;
    private Integer vout;
    private ScriptSig scriptSig;
    private Long sequence;
    private List<String> txInWitness;
}