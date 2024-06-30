package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The implementation of the user service entity
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDao userDao) {
        this.userRepository = userRepository;
        this.userDao = userDao;
    }
    @Override
    public User getUserById(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getUsers();
        //return userRepository.findAll();
    }

    @Override
    public String getUsernameByAccountId(Integer accountId) {
        return userRepository.getUsernameByAccountId(accountId);
    }

}
