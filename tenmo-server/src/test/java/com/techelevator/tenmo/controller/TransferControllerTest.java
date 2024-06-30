package com.techelevator.tenmo.controller;

import static com.techelevator.tenmo.controller.LoginUtils.getTokenForLogin;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.Transfer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static String testUserToken;
    private static int testUserId = 1005;

    @Before
    public void setUp() throws Exception {
        SecurityContextHolder.clearContext();
        testUserToken = getTokenForLogin("test", "test", mvc);
    }

    @Test
    public void checkControllerSecurity() throws Exception{
        if (!isControllerSecure()) {
            fail("Authentication & Authorization not enabled for TransferController.create()");
        }
    }

    @Test
    public void shouldPass_createSendTransfer() throws Exception{
        
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setAmount(new BigDecimal(50.00));
        transfer.setAccountFromId(testUserId);
        transfer.setAccountToId(1002);
        

        mvc.perform(post("/transfer?userFromId=" + transfer.getAccountFromId() + "&userToId=" + transfer.getAccountToId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(transfer))
                .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFail_createSendTransferUsingWrongUserFromId() throws Exception{
        
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setAmount(new BigDecimal(50.00));
        transfer.setAccountFromId(1001);
        transfer.setAccountToId(1002);
        

        mvc.perform(post("/transfer?userFromId=" + transfer.getAccountFromId() + "&userToId=" + transfer.getAccountToId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(transfer))
                .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFail_createSendTransferInsufficientFunds() throws Exception{
        
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setAmount(new BigDecimal(50000.00));
        transfer.setAccountFromId(testUserId);
        transfer.setAccountToId(1002);
        

        mvc.perform(post("/transfer?userFromId=" + transfer.getAccountFromId() + "&userToId=" + transfer.getAccountToId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(transfer))
                .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFail_createSendTransferInvalidAmount() throws Exception{
        
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(2);
        transfer.setAmount(new BigDecimal(0));
        transfer.setAccountFromId(testUserId);
        transfer.setAccountToId(1002);
        

        mvc.perform(post("/transfer?userFromId=" + transfer.getAccountFromId() + "&userToId=" + transfer.getAccountToId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(transfer))
                .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldPass_createRequestTransfer() throws Exception{
        
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(1); // request
        transfer.setAmount(new BigDecimal(50.00));
        transfer.setAccountFromId(1002);
        transfer.setAccountToId(1005);
        

        mvc.perform(post("/transfer?userFromId=" + transfer.getAccountFromId() + "&userToId=" + transfer.getAccountToId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(transfer))
                .header("Authorization", "Bearer " + testUserToken))
                .andExpect(status().isCreated());

    }
    /**
     * Checks to make sure the {@PreAuthorize} annotation was added to the class
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private boolean isControllerSecure() throws InvocationTargetException, IllegalAccessException {
        boolean isControllerSecure = false;
        for (Annotation annotation : TransferController.class.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (type.getName().equals("org.springframework.security.access.prepost.PreAuthorize")) {
                for (Method method : type.getDeclaredMethods()) {
                    Object value = method.invoke(annotation, (Object[])null);
                    if (value.equals("isAuthenticated()")) {
                        isControllerSecure = true;
                        break;
                    }
                }
            }
        }
        return isControllerSecure;
    }
    
    private String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
