package com.techelevator.unit;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.UserRepository;
import com.techelevator.tenmo.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUserByAccountName_WithAccountId_ReturnsUser() {
        User user = new User();
        user.setUsername("Tenmo");
        user.setId(1000);

        Account account = new Account();
        account.setAccountId(2000);
        account.setUser(user);

        when(userRepository.getUsernameByAccountId(account.getAccountId())).thenReturn(user.getUsername());

        String username = userService.getUsernameByAccountId(2000);

        assertNotNull(username);
        assertEquals(user.getUsername(), username);
    }

}
