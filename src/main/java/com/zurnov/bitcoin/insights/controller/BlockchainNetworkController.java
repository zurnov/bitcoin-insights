package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import com.zurnov.bitcoin.insights.service.BlockchainNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlockchainNetworkController {

    BlockchainNetworkService blockchainNetworkService;

    @Autowired
    public BlockchainNetworkController(BlockchainNetworkService blockchainNetworkService) {
        this.blockchainNetworkService = blockchainNetworkService;
    }

    @GetMapping("/getblockchaininfo")
    public ResponseEntity<BlockchainNetworkInfoDTO> getBlockchainNetworkInfo() {

        BlockchainNetworkInfoDTO result = blockchainNetworkService.getBlockchainNetworkInfo();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
