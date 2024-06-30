package com.techelevator.tenmo.configuration;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.repository.UserRepository;
import com.techelevator.tenmo.service.UserServiceImpl;
import com.techelevator.tenmo.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration for UserService class that initializes beans.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@Configuration
public class UserServiceConfig {
    /**
     * This bean will be used when @Autowired is used.
     * @param userRepository - Interface of the repository to be used
     * @return The implementation class of the service to be used
     */
    @Bean
    public UserService userService(UserRepository userRepository, UserDao userDao) {
        return new UserServiceImpl(userRepository, userDao);
    }
}
