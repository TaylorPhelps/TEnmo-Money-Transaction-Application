package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public List<Transfer> getTransferHistoryByUserId(Integer userId) {
        return transferRepository.findAllById(Collections.singleton(userId));
    }

    @Override
    public List<Transfer> findAllProjectionByUserId(Integer userId) {
        return transferRepository.getAllByAccountFromByUserId(userId);
    }

    @Override
    public List<Transfer> getAllByAccountToByUserId(Integer userId) {
        return transferRepository.getAllByAccountToByUserId(userId);
    }

}
