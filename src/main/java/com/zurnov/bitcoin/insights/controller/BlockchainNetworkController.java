package com.zurnov.bitcoin.insights.controller;

import com.zurnov.bitcoin.insights.dto.BlockDTO;
import com.zurnov.bitcoin.insights.dto.BlockchainNetworkInfoDTO;
import com.zurnov.bitcoin.insights.dto.RawTransactionInfoDTO;
import com.zurnov.bitcoin.insights.service.BlockchainBlockService;
import com.zurnov.bitcoin.insights.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BlockchainNetworkController {

    BlockchainBlockService blockchainBlockService;

    @Autowired
    public BlockchainNetworkController(BlockchainBlockService blockchainBlockService) {
        this.blockchainBlockService = blockchainBlockService;
    }

    @GetMapping("/getblockchaininfo")
    public ResponseEntity<BlockchainNetworkInfoDTO> getBlockchainNetworkInfo() {

        String requestId = RequestUtil.generateRequestId();
        log.info(">> Received new request for getBlockchainNetworkInfo ID: {}", requestId);
        BlockchainNetworkInfoDTO result = blockchainBlockService.getBlockchainNetworkInfo(requestId);

        log.info("<< getBlockchainNetworkInfo ID: {}\n", requestId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getblockinfobyhash/{blockHash}")
    public ResponseEntity<BlockDTO> getBlockInfoByHash(
            @PathVariable String blockHash,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {

        String requestId = RequestUtil.generateRequestId();
        log.info(">> Received new request for getBlockInfoByHash ID: {}", requestId);
        BlockDTO result = blockchainBlockService.getBlockInfoByHash(blockHash, pageNumber, pageSize, requestId);
        log.info("<< getBlockInfoByHash ID: {}\n", requestId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getblockinfobyheight/{blockHeight}")
    public ResponseEntity<BlockDTO> getBlockInfoByHeight(
            @PathVariable Integer blockHeight,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {

        String requestId = RequestUtil.generateRequestId();
        log.info(">> Received new request for getBlockInfoByHeight ID: {}", requestId);
        BlockDTO result = blockchainBlockService.getBlockInfoByHeight(blockHeight, pageNumber, pageSize, requestId);
        log.info("<< getBlockInfoByHeight ID: {}\n", requestId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/gettransactioninfo/{transactionHash}")
    public ResponseEntity<RawTransactionInfoDTO> getRawTransactionInfo(@PathVariable String transactionHash) {

        String requestId = RequestUtil.generateRequestId();
        log.info(">> Received new request for getRawTransactionInfo ID: {}", requestId);
        RawTransactionInfoDTO result = blockchainBlockService.getRawTransactionInfo(transactionHash, requestId);
        log.info("<< getRawTransactionInfo ID: {}\n", requestId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
