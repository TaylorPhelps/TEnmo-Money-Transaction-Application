package com.techelevator.tenmo.services;

import com.techelevator.tenmo.TransferTypes;
import com.techelevator.tenmo.Util;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.services.interfaces.Restable;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * AccountService calls REST API methods that were created on the server
 * side of the TEnmo app.
 * @author Ja'Michael Garcia
 * @version 1.0
 * @since 2024-06-06
 */
public class AccountService implements Restable {
    private RestTemplate restTemplate = new RestTemplate();
    private final String accountUrl = "account/";
    /**
     * Gets the total amount from all the user's accounts
     * @param user  The authenticated user that is requesting to check their balance
     * @param url   REST API url to use for our GET request
     * @return      The total amount the user has in BigDecimal form. Can return null if the user or balance is not found
     */
    public BigDecimal viewCurrentBalance(AuthenticatedUser user, String url){
        BigDecimal balance = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url + accountUrl + "balance/?userId=" + user.getUser().getId(),
                    HttpMethod.GET, request, BigDecimal.class);
            balance = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    private HttpHeaders createHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    public Integer createTransfer(AuthenticatedUser loggedInUser, String apiBaseUrl, 
            int otherUserId, BigDecimal amount, TransferTypes transferType){
        //Create a transfer object with the necessary information
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId( transferType.value);
        transfer.setTransferStatusId(1);
        if(transferType == TransferTypes.SEND) {
            // send request, money will come out of logged in user's account
            transfer.setAccountFromId(loggedInUser.getUser().getId());
            transfer.setAccountToId(otherUserId);  
        } else {
            // transfer request, money will come out of the specified user's account
            transfer.setAccountFromId(otherUserId);
            transfer.setAccountToId(loggedInUser.getUser().getId());
        }
        
        
        transfer.setAmount(amount);
        
        //Make the POST request
        try{
            HttpEntity<Transfer> request = new HttpEntity<>(transfer, createHttpHeaders(loggedInUser.getToken()));
            Transfer returnedTransfer = restTemplate.postForObject(apiBaseUrl + "transfer/?" + 
                "userFromId=" + transfer.getAccountFromId() + "&" +
                "userToId=" + transfer.getAccountToId(), request, Transfer.class);
                
            return returnedTransfer.getTransferId();
        } 
        catch(HttpClientErrorException | HttpServerErrorException e){
            String messageString = e.getMessage();
            if(messageString.contains("Not enough funds"))
                return -2;
            BasicLogger.log(e.getMessage());
        }

        catch(RestClientResponseException | ResourceAccessException e){

            BasicLogger.log(e.getMessage());
            
        }
        return -1;
    }

    /**
     * Updates the balance of the account passed in.
     * @param user  The authenticated user that is requesting to check their balance
     * @param url   REST API url to use for our GET request
     * @param account Account to update
     */
    public void updateCurrentBalance(AuthenticatedUser user, Account account, String url){
        try {
            HttpEntity<Account> request = makeAuthEntityWithBody(user.getToken(), account);
           restTemplate.exchange(url + accountUrl + "balance/update",
                    HttpMethod.PUT, request, Void.class);

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public Account getAccount(AuthenticatedUser user, Integer accountId, String url){
        Account account = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            ResponseEntity<Account> response = restTemplate.exchange(url + accountUrl + "information/?accountId=" + accountId,
                    HttpMethod.GET, request, Account.class);
            account = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    private HttpEntity<Account> makeAuthEntityWithBody(String token, Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(account, headers);
    }
}
