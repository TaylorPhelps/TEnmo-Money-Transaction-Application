package com.techelevator.tenmo.controller;

import static com.techelevator.tenmo.controller.LoginUtils.getTokenForLogin;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.service.TransferTypeService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferTypeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransferTypeService transferTypeService;

    private static String testUserToken;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();
        testUserToken = getTokenForLogin("test", "test", mvc);
    }

    @Test
    public void checkControllerSecurity() throws Exception {
        if (!isControllerSecure()) {
            fail("Authentication & Authorization not enabled for TransferTypeController.getTransferTypeByTransferTypeId()");
        }
    }

    @Test
    public void shouldPass_getTransferTypeByTransferTypeId() throws Exception {
        TransferType transferType = new TransferType();
        transferType.setTransferTypeDesc("Send");

        when(transferTypeService.getTransferTypeByTransferTypeId(anyInt())).thenReturn(transferType);

        mvc.perform(get("/transfer/type")
                        .param("transferId", "1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Send"));
    }

    @Test
    public void shouldFail_getTransferTypeByTransferTypeIdInvalidTransferId() throws Exception {
        when(transferTypeService.getTransferTypeByTransferTypeId(anyInt())).thenThrow(new IllegalArgumentException("Invalid transfer ID"));

        mvc.perform(get("/transfer/type")
                        .param("transferId", "invalid")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFail_getTransferTypeByTransferTypeIdNotFound() throws Exception {
        when(transferTypeService.getTransferTypeByTransferTypeId(anyInt())).thenReturn(null);

        mvc.perform(get("/transfer/type")
                        .param("transferId", "1")
                        .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isNotFound());
    }

    private boolean isControllerSecure() throws InvocationTargetException, IllegalAccessException {
        boolean isControllerSecure = false;
        for (Annotation annotation : TransferTypeController.class.getAnnotations()) {
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
