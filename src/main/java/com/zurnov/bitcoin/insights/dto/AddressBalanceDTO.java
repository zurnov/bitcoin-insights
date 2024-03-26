package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressBalanceDTO {

    private Long confirmed;
    private Long unconfirmed;
    private BigDecimal confirmedBitcoin;
    private BigDecimal unconfirmedBitcoin;

    public AddressBalanceDTO(Long confirmed, Long unconfirmed) {
        this.confirmed = confirmed;
        this.unconfirmed = unconfirmed;
    }

    public BigDecimal getConfirmedBitcoin() {
        BigDecimal bitcoin = BigDecimal.valueOf(0);

        if (confirmed != 0) {
            bitcoin = BigDecimal.valueOf(confirmed).divide(BigDecimal.valueOf(100000000), 8, RoundingMode.HALF_UP);
        }

        return bitcoin;
    }

    public BigDecimal getUnconfirmedBitcoin() {
        BigDecimal bitcoin = BigDecimal.valueOf(0);

        if (unconfirmed != 0) {
            bitcoin = BigDecimal.valueOf(unconfirmed).divide(BigDecimal.valueOf(100000000), 8, RoundingMode.HALF_UP);
        }

        return bitcoin;
    }

}
