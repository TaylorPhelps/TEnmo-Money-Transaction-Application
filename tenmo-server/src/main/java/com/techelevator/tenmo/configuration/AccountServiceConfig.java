package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.repository.AccountRepository;
import com.techelevator.tenmo.service.AccountService;
import com.techelevator.tenmo.service.AccountServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for AccountService class that initializes beans.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@Configuration
public class AccountServiceConfig {

    /**
     * This bean will be used when @Autowired is used.
     * @param accountRepository - The repository to be used
     * @return The implementation class of the service to be used
     */
    @Bean
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountServiceImpl(accountRepository);
    }
}
