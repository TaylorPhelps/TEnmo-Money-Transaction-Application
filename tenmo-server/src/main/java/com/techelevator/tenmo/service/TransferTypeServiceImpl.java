package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.repository.TransferTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferTypeServiceImpl implements TransferTypeService {
    private final TransferTypeRepository transferTypeRepository;

    @Autowired
    public TransferTypeServiceImpl(TransferTypeRepository transferTypeRepository) {
        this.transferTypeRepository = transferTypeRepository;
    }

    @Override
    public TransferType getTransferTypeByTransferTypeId(Integer transferId) {
        return transferTypeRepository.getTransferTypeByTransferTypeId(transferId);
    }
}
