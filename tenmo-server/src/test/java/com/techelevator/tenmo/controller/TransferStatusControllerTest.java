package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.service.TransferStatusService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferStatusControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransferStatusService transferStatusService;

    private String testUserToken;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();
        testUserToken = LoginUtils.getTokenForLogin("test", "test", mvc); // Correct usage of getTokenForLogin
    }

    @Test
    public void checkControllerSecurity() throws Exception {
        // Check if the controller method is secured with @PreAuthorize("isAuthenticated()")
        boolean isSecure = isControllerMethodSecured(TransferStatusController.class, "getTransferStatusName");
        if (!isSecure) {
            throw new AssertionError("Authentication & Authorization not enabled for TransferStatusController.getTransferStatusName()");
        }
    }

    @Test
    public void shouldPass_getTransferStatusName() throws Exception {
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransferStatusDesc("Approved");

        when(transferStatusService.getTransferStatusName(anyInt())).thenReturn(transferStatus);

        mvc.perform(get("/transfer/status")
                        .param("transferId", "1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Approved"));
    }

    @Test
    public void shouldFail_getTransferStatusNameInvalidTransferId() throws Exception {
        when(transferStatusService.getTransferStatusName(anyInt())).thenThrow(new IllegalArgumentException("Invalid transfer ID"));

        mvc.perform(get("/transfer/status")
                        .param("transferId", "invalid")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFail_getTransferStatusNameNotFound() throws Exception {
        when(transferStatusService.getTransferStatusName(anyInt())).thenReturn(null);

        mvc.perform(get("/transfer/status")
                        .param("transferId", "1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isNotFound());
    }

    // Method to check if a controller method is secured with @PreAuthorize("isAuthenticated()")
    private boolean isControllerMethodSecured(Class<?> controllerClass, String methodName) {
        try {
            Method method = controllerClass.getMethod(methodName, Integer.class); // Adjust parameter type if needed
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(PreAuthorize.class)) {
                    PreAuthorize preAuthorize = (PreAuthorize) annotation;
                    return preAuthorize.value().equals("isAuthenticated()");
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }
}
