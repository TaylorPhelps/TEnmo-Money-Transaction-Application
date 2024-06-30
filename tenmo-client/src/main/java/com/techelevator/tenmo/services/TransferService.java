package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.interfaces.Restable;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TransferService implements Restable {
    private final RestTemplate restTemplate = new RestTemplate();

    private final String BASE_URL;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final String transferUrl = "transfer/";

    public TransferService(String baseUrl, AuthenticationService authenticationService, UserService userService) {
        this.BASE_URL = baseUrl;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    /**
     * Gets all transaction history tied to the user ID.
     * @param user The user who is requesting their transaction history.
     * @return Transfer objects in a list container.
     */
    public List<Transfer> getTransferHistory(AuthenticatedUser user){
        List<Transfer> transfers = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            ResponseEntity<List<Transfer>> response = restTemplate.exchange(BASE_URL + transferUrl + "history/?userId=" + user.getUser().getId(),
                    HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                    } );
            transfers = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }


    /**
     * Gets details of a specific transfer by its ID.
     * @param user The authenticated user making the request.
     * @param transferId The ID of the transfer to retrieve details for.
     * @return Transfer object containing details of the specific transfer.
     */
    public Transfer getTransferDetails(AuthenticatedUser user, int transferId) {
        Transfer transfer = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + transferUrl + transferId,
                    HttpMethod.GET, request, Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    /**
     * Returns the type of transfer associated with the transfer id.
     * @param token Authentication bearer
     * @param transferId The transfer to search for
     * @return Either "Request" or "Send".
     */
    public String getTransferType(String token, Integer transferId) {
        String transferType = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(token);
            ResponseEntity<String> response = restTemplate.exchange(BASE_URL + transferUrl + "type/?transferId=" + transferId,
                    HttpMethod.GET, request, String.class);
            transferType = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferType;
    }

    /**
     * Returns the status of transfer associated with the transfer id.
     * @param token Authentication bearer
     * @param transferId The transfer to search for
     * @return Either "Request" or "Send".
     */
    public String getTransferStatus(String token, Integer transferId) {
        String transferStatus = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(token);
            ResponseEntity<String> response = restTemplate.exchange(BASE_URL + transferUrl + "status/?transferId=" + transferId,
                    HttpMethod.GET, request, String.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatus;
    }

    /**
     * Creates a new transfer with a POST request.
     * @param token Authentication token needed for header
     * @param requestAmount The amount that is being requested in TE bucks
     * @return Results of the transfer as a Transfer object
     */
    public Transfer postTransfer(String token, BigDecimal requestAmount) {
        //  TODO: Implement client side call for Use Case #7
        Transfer transferResult = null;
        try {
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferResult;
    }

    public List<Transfer> getPendingTransfers(AuthenticatedUser user) {
        List<Transfer> pendingTransfers = null;
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            ResponseEntity<List<Transfer>> response = restTemplate.exchange(BASE_URL + transferUrl + "pending/?userId=" + user.getUser().getId(),
                    HttpMethod.GET, request, new ParameterizedTypeReference<>() {
                    });
            pendingTransfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingTransfers;
    }

    public boolean approveTransfer(AuthenticatedUser user, int transferId) {
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            restTemplate.exchange(BASE_URL + transferUrl + "approve/" + transferId, HttpMethod.PUT, request, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    public boolean rejectTransfer(AuthenticatedUser user, int transferId) {
        try {
            HttpEntity<Void> request = makeAuthEntity(user.getToken());
            restTemplate.exchange(BASE_URL + transferUrl + "reject/" + transferId, HttpMethod.PUT, request, Void.class);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

}
