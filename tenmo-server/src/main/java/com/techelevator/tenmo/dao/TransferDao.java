package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
    public Transfer createTransfer(Transfer transfer);
    public Transfer getTransferById(Integer transferId);
    public Transfer updateTransferStatus(int transferId, int transferStatusId);
}
