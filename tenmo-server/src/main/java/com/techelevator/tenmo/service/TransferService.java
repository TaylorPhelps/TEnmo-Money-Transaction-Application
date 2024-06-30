package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferService {
    List<Transfer> getTransferHistoryByUserId(Integer userId);
    List<Transfer> findAllProjectionByUserId(Integer userId);
    List<Transfer> getAllByAccountToByUserId(Integer userId);
}
