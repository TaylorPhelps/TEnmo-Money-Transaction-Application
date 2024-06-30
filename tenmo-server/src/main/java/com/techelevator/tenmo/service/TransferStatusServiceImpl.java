package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.repository.TransferStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransferStatusServiceImpl implements TransferStatusService {
private TransferStatusRepository transferStatusRepository;
    @Override
    public List<TransferStatus> getTransferStatus() {
        return null;
    }

    @Override
    public TransferStatus getTransferStatusName(Integer transferId) {
        return transferStatusRepository.getTransferStatusName(transferId);
    }
    @Autowired
    public TransferStatusServiceImpl (TransferStatusRepository transferStatusRepository) {
        this.transferStatusRepository = transferStatusRepository;
    }
}
