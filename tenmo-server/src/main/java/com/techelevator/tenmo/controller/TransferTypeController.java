package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.service.TransferTypeService;
import com.techelevator.tenmo.service.TransferTypeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer/type")
public class TransferTypeController {
    private final TransferTypeService transferTypeService;

    @Autowired
    public TransferTypeController( TransferTypeServiceImpl transferTypeService) {
        this.transferTypeService = transferTypeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getTransferTypeByTransferTypeId(@RequestParam @Valid Integer transferId, Principal principal) {
        log.info("[{}]: {} is currently requesting the transfer type for transfer ID: {} for {}", LocalDateTime.now(), "Tenmo Server", transferId, principal.getName());
        TransferType transferType = transferTypeService.getTransferTypeByTransferTypeId(transferId);
        String transferDescription = null;

        if (transferType != null) {
            transferDescription = transferType.getTransferTypeDesc();
        }
        else{
            log.error("[{}]: {} is unable to get access to the transfer type for the transfer ID requested.", LocalDateTime.now(), "Tenmo Server");
        }

        return transferDescription;
    }
}
