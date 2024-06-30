package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.service.TransferStatusService;
import com.techelevator.tenmo.service.TransferStatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/transfer/status")
public class TransferStatusController {
    private final TransferStatusService transferStatusService;
@Autowired
    public TransferStatusController(TransferStatusServiceImpl transferStatusServiceImpl) {
        this.transferStatusService = transferStatusServiceImpl;
    }



@GetMapping
    public String getTransferStatusName(@RequestParam @Valid Integer transferId) {
    return transferStatusService.getTransferStatusName(transferId).getTransferStatusDesc();
}

}
