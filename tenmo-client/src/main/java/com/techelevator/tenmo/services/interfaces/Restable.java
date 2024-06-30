package com.techelevator.tenmo.services.interfaces;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public interface Restable {
    /**
     * Helper method to create the HttpHeader for use of our RestTemplate.exchange method calls.
     * @param token The bearer token of the user currently authenticated.
     * @return An initialized HttpEntity that is set up with the bearer token
     */
    default HttpEntity<Void> makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
