package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.service.TransferService;
import com.techelevator.tenmo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.techelevator.tenmo.Util;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.AccountDaoModel;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.TransferStatus.Status;

import javax.validation.Valid;


import lombok.extern.slf4j.Slf4j;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
@Slf4j
public class TransferController {
   private TransferDao transferDao;
   private AccountDao accountDao;
   private final TransferService transferService;
   private UserService userService;

   @Autowired
   public TransferController(TransferDao transferDao, AccountDao accountDao, TransferService transferService, UserService userService){
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.transferService = transferService;
        this.userService = userService;
   }

    /**
     * Returns all the users transfer history information.
     * @param userId The user who is requesting the data
     * @param principal Used for logging in the system when this request was created
     * @return A collection of transfers in a List container.
     */
   @GetMapping("history")
   @ResponseStatus(HttpStatus.OK)
   public List<Transfer> getAllTransferHistory(@RequestParam @Valid Integer userId, Principal principal){
       log.info("[{}]: {} is currently requesting to view their transaction history.", LocalDateTime.now(), principal.getName());
       List<Transfer> transfers;
       transfers = transferService.findAllProjectionByUserId(userId);

       if(transfers == null){
           log.error("[{}]: {} is unable to view their transaction history at this time.", LocalDateTime.now(), principal.getName());
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid request");
       }else{
           transfers.addAll(transferService.getAllByAccountToByUserId(userId));

           // This is to fix the return because of the DAO/JPA setup initially was not correct
           for(Transfer transfer : transfers){
               transfer.setTransferStatusId(transferDao.getTransferById(transfer.getTransferId()).getTransferStatusId());
               transfer.setTransferTypeId(transferDao.getTransferById(transfer.getTransferId()).getTransferTypeId());
           }
       }



       return transfers;
   }

    /**
    * Creates a send-transfer.
    * - makes sure that from & to accounts are not identical
    * - makes sure that there are enough funds in the from account
    * @param transfer A valid transfer object
    * @param principal Current user
    * @return Returns the newly created transfer object
    */

   @ResponseStatus(HttpStatus.CREATED)
   @RequestMapping(method = RequestMethod.POST)
   public ResponseEntity<Transfer> createTransfer(@RequestBody @Valid Transfer transfer, Principal principal,
   @RequestParam Integer userFromId,
   @RequestParam Integer userToId) {

        
        if(transfer.getTransferTypeId().equals(TransferType.Type.SEND.value)) // 2
        {
            
            transfer.setTransferStatusId(Status.APPROVED.value); // 2
            if(!principal.getName().equals(userService.getUserById(userFromId).getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account from id does not belong to the principle user");
            }

        } else if(transfer.getTransferTypeId().equals(TransferType.Type.REQUEST.value)) { // 1
            transfer.setTransferStatusId(Status.PENDING.value); // 1
            if(!principal.getName().equals(userService.getUserById(userToId).getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account to id does not belong to the principle user");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transfer type");
        }

        if(userFromId.equals(userToId))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'From' and 'to' accounts cannot be the same.");
        }

        try {

            transfer.setAccountFromId(accountDao.getAccountIdByUserId(userFromId));

            AccountDaoModel accountFrom = null;
            // if this is a send transfer make sure there are enough funds in the account
            if(transfer.getTransferTypeId().equals(TransferType.Type.SEND.value))
            {
                accountFrom = accountDao.getAccountById(transfer.getAccountFromId());
                if(transfer.getAmount().compareTo(accountFrom.getBalance()) > 0 )
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough funds in the account.");
            }

            transfer.setAccountToId(accountDao.getAccountIdByUserId(userToId));

            // create the transfer first before updating balances
            Transfer returnTransfer =  transferDao.createTransfer(transfer);

            // if this is a send transfer update the balances on both accounts
            if(transfer.getTransferTypeId().equals(TransferType.Type.SEND.value)) {
                AccountDaoModel accountTo = accountDao.getAccountById(transfer.getAccountToId());
                accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
                this.accountDao.updateAccount(accountTo);

                accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getAmount()));
                this.accountDao.updateAccount(accountFrom);
            }

            // log the transfer
            if(principal!=null)
                log.info("[{}]: {} initiated a {} transfer to {} for {}.", LocalDateTime.now(),
                    principal.getName(),
                    transfer.getTransferTypeId().toString(),
                    principal.getName() , transfer.getAmount());

            return ResponseEntity.status(HttpStatus.CREATED).body(returnTransfer);
        } catch (DaoException exception) {
            log.error( "transferDao:createSend" , exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to initiate the transfer.");
        }
   }

   @ResponseStatus(HttpStatus.OK)
   @RequestMapping(path = "/{id}", method = RequestMethod.GET)
   public Transfer getTransfer(@PathVariable Integer id) {
        try{
            return transferDao.getTransferById(id);
        } catch (DaoException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem getting transfer " + id);
        }
    }

    /**
     * Returns the authenticated users pending transfers
     * @param userId The user who is requesting the data
     * @param principal Used for logging in the system when this request was created
     * @return A collection of transfers in a List container.
     */
    @GetMapping("pending")
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> getAllPendingTransfersForUser(@RequestParam @Valid Integer userId, Principal principal){
        log.info("[{}]: {} is currently requesting to view their {} transaction history.", LocalDateTime.now(), principal.getName(), Status.PENDING );
        List<Transfer> transferHistory = transferService.findAllProjectionByUserId(userId);

        List<Transfer> pendingTransfers = new ArrayList<>();
        if(!transferHistory.isEmpty()){
            for(Transfer transfer : transferHistory){
                if(transfer.getTransferStatus().getTransferStatusDesc().equalsIgnoreCase(Status.PENDING.toString())){
                    pendingTransfers.add(transfer);
                }
            }
            log.info("[{}]: API call is generating {}'s {} transactions.", LocalDateTime.now(), principal.getName(), Status.PENDING );
        }else{
            log.error("[{}]: {} doesn't have any {} transactions to review.", LocalDateTime.now(), principal.getName(), Status.PENDING );
        }

        return pendingTransfers;
    }

    @PutMapping("approve/{transferId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void approveTransfer(@PathVariable @Valid Integer transferId, Principal principal) {
        log.info("[{}]: {} has {} of the transfer request ID: {}.", LocalDateTime.now(), principal.getName(), Status.APPROVED, transferId );
        transferDao.updateTransferStatus(transferId, Status.APPROVED.value);
    }

    @PutMapping("reject/{transferId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void rejectTransfer(@PathVariable @Valid Integer transferId, Principal principal) {
        log.info("[{}]: {} has {} of the transfer request ID: {}.", LocalDateTime.now(), principal.getName(), Status.REJECTED, transferId );
        transferDao.updateTransferStatus(transferId, Status.REJECTED.value);
    }
}
