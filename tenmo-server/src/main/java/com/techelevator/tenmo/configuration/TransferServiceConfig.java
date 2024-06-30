package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.repository.TransferRepository;
import com.techelevator.tenmo.service.TransferService;
import com.techelevator.tenmo.service.TransferServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferServiceConfig {
    @Bean
    public TransferService transferService(TransferRepository transferRepository) {
        return new TransferServiceImpl(transferRepository);
    }
}
