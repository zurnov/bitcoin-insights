package com.zurnov.bitcoin.insights.dto;

import lombok.Data;

@Data
public class AssetDTO {

    private Long assetId;
    private String assetName;
    private String symbol;
    private Double currentPrice;

}

