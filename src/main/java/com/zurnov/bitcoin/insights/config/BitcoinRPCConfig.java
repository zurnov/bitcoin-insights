package com.zurnov.bitcoin.insights.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Configuration
public class BitcoinRPCConfig {

    @Value("${rpc.user}")
    private String rpcUser;
    @Value("${rpc.password}")
    private String rpcPassword;
    @Value("${rpc.address}")
    private String rpcAddress;

}
