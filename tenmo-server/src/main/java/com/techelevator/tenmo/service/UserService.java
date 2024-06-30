package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.User;

import java.util.List;

/**
 * Our user service that will allow new implementations
 * and features when needed. Always create new methods
 * within this interface.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
public interface UserService {
    User getUserById(Integer id);
    List<User> getAllUsers();
    String getUsernameByAccountId(Integer accountId);
}
