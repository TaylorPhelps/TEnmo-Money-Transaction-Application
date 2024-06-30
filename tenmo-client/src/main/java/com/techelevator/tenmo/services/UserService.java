package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.interfaces.Restable;
import com.techelevator.util.BasicLogger;

/**
 * UserService calls REST API methods created on the server
 *  * side of the TEnmo app.
 *  * @author Ja'Michael Garcia
 *  * @version 1.0
 *  * @since 2024-06-06
 */


 public class UserService implements Restable {
    
    private RestTemplate restTemplate = new RestTemplate();
    private final String userUrl = "user/";

     public List<User> getAllUsers(AuthenticatedUser user, String baseURL){
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            ResponseEntity<List<User>> response = restTemplate.exchange( baseURL + userUrl,
                    HttpMethod.GET, request , 
                    new ParameterizedTypeReference<List<User>>() {});
                    return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    /**
     * Get the username associated with the account
     * @param token Authentication bearer
     * @param accountId The user to search for
     * @param url REST API url to make request to
     * @return The username.
     */
    public String getUsername(String token, Integer accountId, String url){
         String username = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(token);
            ResponseEntity<String> response = restTemplate.exchange( url + userUrl + "username/?accountId=" + accountId.toString(),
                    HttpMethod.GET, request,
                    String.class);
            username = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }
}
