package com.zurnov.bitcoin.insights.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressTransactionHistoryDTO {

    private List<TransactionDTO> transactions;

}
