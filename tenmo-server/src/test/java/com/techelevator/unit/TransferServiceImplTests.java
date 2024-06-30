package com.techelevator.unit;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.TransferRepository;
import com.techelevator.tenmo.service.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

public class TransferServiceImplTests {
    private Account account;
    private User user;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferServiceImpl transferServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        account = new Account();
        user = new User();
    }

    @Test
    public void getTransfersFromUser_WhenUserIdIsValid_ReturnsSendTransfers() {
        when(transferRepository.getAllByAccountFromByUserId(user.getId())).thenReturn(List.of());
    }
}
