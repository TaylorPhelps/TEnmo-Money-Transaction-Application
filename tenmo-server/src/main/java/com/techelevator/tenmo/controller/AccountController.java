package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.service.AccountServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;

/**
 * The controller for Account class. Provides REST API calls for getting
 * a-users balance
 * Future implementation should be added here
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getTotalBalance(@RequestParam @Valid Integer userId, Principal principal) {
        log.info("[{}]: {} is currently requesting to check their balance.", LocalDateTime.now(), principal.getName());
        return accountService.getTotalBalance(userId);
    }

    @GetMapping("/information")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByAccountId(@RequestParam @Valid Integer accountId, Principal principal) {
        log.info("[{}]: REST API is currently requesting account information {}.", LocalDateTime.now(), principal.getName());
        return accountService.getAccountByAccountId(accountId);
    }

    @PutMapping("/balance/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateAccountBalance(@RequestBody @Valid Account account,  Principal principal) {
        log.info("[{}]: Updating the balance for Account ID: {} which was initiated by {}.", LocalDateTime.now(), account.getAccountId(), principal.getName());
        accountService.updateBalanceByAccountId(account.getAccountId(), account.getBalance());
    }
}
