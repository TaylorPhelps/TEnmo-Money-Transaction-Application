package com.techelevator.tenmo.controller;

import static com.techelevator.tenmo.controller.LoginUtils.getTokenForLogin;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    private static String testUserToken;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();
        testUserToken = getTokenForLogin("test", "test", mvc);
    }

    @Test
    public void checkControllerSecurity() throws Exception {
        if (!isControllerSecure()) {
            fail("Authentication & Authorization not enabled for UserController.");
        }
    }

    @Test
    public void shouldPass_getAllUsers() throws Exception {
        User user1 = new User(1, "user1", "password1", "USER");
        User user2 = new User(2, "user2", "password2", "USER");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mvc.perform(get("/user")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'username':'user1'},{'id':2,'username':'user2'}]"));
    }

    @Test
    public void shouldPass_getUserById() throws Exception {
        User user = new User(1, "user1", "password1", "USER");

        when(userService.getUserById(anyInt())).thenReturn(user);

        mvc.perform(get("/user/1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'username':'user1'}"));
    }

    @Test
    public void shouldFail_getUserByIdNotFound() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(null);

        mvc.perform(get("/user/1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldPass_getUsernameByAccountId() throws Exception {
        String username = "user1";

        when(userService.getUsernameByAccountId(anyInt())).thenReturn(username);

        mvc.perform(get("/user/username")
                        .param("accountId", "1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(content().string("user1"));
    }

    @Test
    public void shouldFail_getUsernameByAccountIdInvalid() throws Exception {
        when(userService.getUsernameByAccountId(anyInt())).thenThrow(new IllegalArgumentException("Invalid account ID"));

        mvc.perform(get("/user/username")
                        .param("accountId", "invalid")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isBadRequest());
    }

    private boolean isControllerSecure() throws InvocationTargetException, IllegalAccessException {
        boolean isControllerSecure = false;
        for (Annotation annotation : UserController.class.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (type.getName().equals("org.springframework.security.access.prepost.PreAuthorize")) {
                for (Method method : type.getDeclaredMethods()) {
                    Object value = method.invoke(annotation, (Object[]) null);
                    if (value.equals("isAuthenticated()")) {
                        isControllerSecure = true;
                        break;
                    }
                }
            }
        }
        return isControllerSecure;
    }
}
