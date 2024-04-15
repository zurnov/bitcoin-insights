package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.BlockDTO;
import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import com.zurnov.bitcoin.insights.service.BlockchainBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
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
    public ResponseEntity<BlockDTO> getBlockInfoByHash(@PathVariable String blockHash) {

        BlockDTO result = blockchainBlockService.getBlockInfoByHash(blockHash);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getblockinfobyheight/{blockHeight}")
    public ResponseEntity<BlockDTO> getBlockInfoByHeight(@PathVariable Integer blockHeight) {

        BlockDTO result = blockchainBlockService.getBlockInfoByHeight(blockHeight);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


//TODO
//    @GetMapping("/gettransactioninfo/{transactionHash}")
//    public ResponseEntity<String> getTransactionInfo(@PathVariable String transactionHash) {
//
//        String result = blockchainBlockService.getTransactionInfo(transactionHash);
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }


}
