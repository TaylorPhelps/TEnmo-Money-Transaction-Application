package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.UserServiceImpl;
import com.techelevator.tenmo.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

/**
 * The controller for User class. Provides REST API calls for getting all registered users.
 * Future implementation should be added here.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-05
 */
@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class UserController {
    private final UserService userService;
    private UserDao userDao;

    @Autowired
    public UserController(UserServiceImpl userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @GetMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    public String getUsernameByAccountId(@RequestParam @Valid Integer accountId){
        return userService.getUsernameByAccountId(accountId);
    }

}
