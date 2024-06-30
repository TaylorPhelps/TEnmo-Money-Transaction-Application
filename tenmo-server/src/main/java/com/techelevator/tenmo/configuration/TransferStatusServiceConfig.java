package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.repository.TransferStatusRepository;
import com.techelevator.tenmo.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferStatusServiceConfig {
    @Bean
    public TransferStatusService transferStatusService(TransferStatusRepository transferStatusRepository) {
        return new TransferStatusServiceImpl(transferStatusRepository);
    }
}
