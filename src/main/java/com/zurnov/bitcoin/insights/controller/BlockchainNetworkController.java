package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.BlockDTO;
import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import com.zurnov.bitcoin.insights.dto.RawTransactionInfoDTO;
import com.zurnov.bitcoin.insights.service.BlockchainBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockchainNetworkController {

    BlockchainBlockService blockchainBlockService;

    @Autowired
    public BlockchainNetworkController(BlockchainBlockService blockchainBlockService) {
        this.blockchainBlockService = blockchainBlockService;
    }

    @GetMapping("/getblockchaininfo")
    public ResponseEntity<BlockchainNetworkInfoDTO> getBlockchainNetworkInfo() {

        BlockchainNetworkInfoDTO result = blockchainBlockService.getBlockchainNetworkInfo();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getblockinfobyhash/{blockHash}")
    public ResponseEntity<BlockDTO> getBlockInfoByHash(
            @PathVariable String blockHash,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {

        BlockDTO result = blockchainBlockService.getBlockInfoByHash(blockHash, pageNumber, pageSize);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getblockinfobyheight/{blockHeight}")
    public ResponseEntity<BlockDTO> getBlockInfoByHeight(
            @PathVariable Integer blockHeight,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {

        BlockDTO result = blockchainBlockService.getBlockInfoByHeight(blockHeight, pageNumber, pageSize);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/gettransactioninfo/{transactionHash}")
    public ResponseEntity<RawTransactionInfoDTO> getRawTransactionInfo(@PathVariable String transactionHash) {

        RawTransactionInfoDTO result = blockchainBlockService.getRawTransactionInfo(transactionHash);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
