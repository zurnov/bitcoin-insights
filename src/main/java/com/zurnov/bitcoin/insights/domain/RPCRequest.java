package com.zurnov.bitcoin.insights.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RPCRequest {

    private String jsonrpc;
    private String id;
    private String method;
    private List<Object> params;

}
