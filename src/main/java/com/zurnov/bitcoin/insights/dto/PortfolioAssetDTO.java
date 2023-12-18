package com.zurnov.bitcoin.insights.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PortfolioAssetDTO {

    private Long portfolioAssetId;
    private Long portfolioId;
    private Long assetId;
    private Double quantity;
    private Double purchasePrice;
    private LocalDate purchaseDate;

}

