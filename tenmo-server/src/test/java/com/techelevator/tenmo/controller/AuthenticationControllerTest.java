package com.techelevator.tenmo.controller;

import static com.techelevator.tenmo.controller.LoginUtils.getTokenForLogin;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.LoginDto;
import com.techelevator.tenmo.model.LoginResponseDto;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserDao userDao;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();
        String testUserToken = getTokenForLogin("test", "test", mvc);
    }

    @Test
    public void shouldPass_login() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test");
        loginDto.setPassword("test");

        User user = new User(1, "test", "password", "USER");
        String token = "jwt-token";

        when(userDao.getUserByUsername("test")).thenReturn(user);
        when(tokenProvider.createToken(any(Authentication.class), Mockito.eq(false))).thenReturn(token);
        when(authenticationManagerBuilder.getObject().authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("test", "test"));

        mvc.perform(post("/login")
                        .contentType("application/json")
                        .content(toJson(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.user.id").value(user.getId()))
                .andExpect(jsonPath("$.user.username").value(user.getUsername()));
    }

    @Test
    public void shouldFail_loginInvalidCredentials() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test");
        loginDto.setPassword("wrongpassword");

        when(authenticationManagerBuilder.getObject().authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        mvc.perform(post("/login")
                        .contentType("application/json")
                        .content(toJson(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldPass_register() throws Exception {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setUsername("newuser");
        newUser.setPassword("password");

        when(userDao.getUserByUsername("newuser")).thenReturn(null);

        mvc.perform(post("/register")
                        .contentType("application/json")
                        .content(toJson(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFail_registerUserAlreadyExists() throws Exception {
        RegisterUserDto newUser = new RegisterUserDto();
        newUser.setUsername("existinguser");
        newUser.setPassword("password");

        User existingUser = new User(1, "existinguser", "password", "USER");

        when(userDao.getUserByUsername("existinguser")).thenReturn(existingUser);

        mvc.perform(post("/register")
                        .contentType("application/json")
                        .content(toJson(newUser)))
                .andExpect(status().isBadRequest());
    }

    private String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
