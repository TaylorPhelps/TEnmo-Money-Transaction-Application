package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.TransferType;

public interface TransferTypeService {
    TransferType getTransferTypeByTransferTypeId(Integer transferId);
}
