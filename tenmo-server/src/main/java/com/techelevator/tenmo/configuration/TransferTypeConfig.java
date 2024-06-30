package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.repository.TransferTypeRepository;
import com.techelevator.tenmo.service.TransferTypeService;
import com.techelevator.tenmo.service.TransferTypeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferTypeConfig {

    @Bean
    public TransferTypeService transferTypeService (TransferTypeRepository transferTypeRepository) {
        return new TransferTypeServiceImpl(transferTypeRepository);
    }
}
