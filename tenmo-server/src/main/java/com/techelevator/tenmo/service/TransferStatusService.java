package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.TransferStatus;

import java.util.List;

public interface TransferStatusService {
    List<TransferStatus> getTransferStatus();

    TransferStatus getTransferStatusName(Integer transferId);


}
