package com.salas.productservice.controller;

import com.salas.common.events.TransferRestModel;
import com.salas.productservice.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private TransferService transferService;

    @Autowired
    public TransferRestController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public boolean transfer(@RequestBody TransferRestModel transferRestModel) {
        return transferService.transfer(transferRestModel);
    }
}
