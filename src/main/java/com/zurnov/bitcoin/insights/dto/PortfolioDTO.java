package com.zurnov.bitcoin.insights.dto;

import lombok.Data;

@Data
public class PortfolioDTO {

    private Long portfolioId;
    private Long userId;
    private String portfolioName;
    private String description;

}

