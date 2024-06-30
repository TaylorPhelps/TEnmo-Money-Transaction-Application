package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.ConsoleService.Colors;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import ch.qos.logback.classic.Level;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService(API_BASE_URL, authenticationService, userService);

    private final Currency currentCurrency = Currency.getInstance("USD");
    private final NumberFormat currencyNumberFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public static void main(String[] args) {
        Logger restTemplateLogger = (Logger) LoggerFactory.getLogger("org.springframework.web.client.RestTemplate");
        restTemplateLogger.setLevel(Level.INFO);
        Logger httpTemplateLogger = (Logger) LoggerFactory.getLogger("org.springframework.web.HttpLogging");
        httpTemplateLogger.setLevel(Level.INFO);
        App app = new App();
        app.run();
    }

    private void run() {
        currencyNumberFormat.setCurrency(currentCurrency);
        consoleService.printGreeting();
        loginMenu();
        
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                consoleService.printColorLine("Invalid Selection", Colors.RED);
            }
            if (currentUser != null) {
                mainMenu();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                createTransfer(TransferTypes.SEND);
            } else if (menuSelection == 5) {
                createTransfer(TransferTypes.REQUEST);
            } else if (menuSelection == 0) {
                currentUser = null;
                continue;
            } else {
               consoleService.printColorLine("Invalid Selection", Colors.RED);
            }
            //consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance = accountService.viewCurrentBalance(currentUser, API_BASE_URL);

        if (balance != null) {
            String currencyBalance = currencyNumberFormat.format(balance);
            consoleService.printCurrentBalance(currencyBalance);
        } else {
            consoleService.printErrorMessage();
        }
    }

    /**
     * Views the transfer history of the current user authenticated
     */
    private void viewTransferHistory() {
        consoleService.printTransferMenu();

        String nameToUse = "";
        String transferType = "";
        String transferAmountFormatted = "";
        String token = currentUser.getToken();
        List<Transfer> transferHistory = transferService.getTransferHistory(currentUser);

        for (Transfer transfer : transferHistory) {
            if (transfer.getTransferTypeId() == TransferTypes.REQUEST.value) {
                transferType = "From";
                nameToUse = userService.getUsername(token, transfer.getAccountFromId(), API_BASE_URL);
            } else {
                transferType = "To";
                nameToUse = userService.getUsername(token, transfer.getAccountToId(), API_BASE_URL);
            }

            if (!nameToUse.equalsIgnoreCase(currentUser.getUser().getUsername())) {
                transferAmountFormatted = currencyNumberFormat.format(transfer.getAmount());
                consoleService.printTransferOverview(transfer.getTransferId(), transferType, nameToUse, transferAmountFormatted);
            }
        }
        System.out.println("---------");
        int input = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel):");
        if (input != 0) {
            Transfer transfer = getTransferId(input, transferHistory);

            if(transfer != null) {
                viewTransferDetails(transfer, token);
            }else{
                consoleService.printColor("Transfer ID: " + input + " is invalid.", Colors.RED);
                viewTransferHistory();
            }
        }
    }

    private Transfer getTransferId(Integer transferId, @NotNull List<Transfer> transferHistory) {
        Transfer transferToReturn = null;
        for (Transfer transfer : transferHistory) {
            if (transfer.getTransferId().equals(transferId)) {
                transferToReturn = transfer;
                break;
            }
        }
        return transferToReturn;
    }

    private void viewTransferDetails(Transfer currentTransfer, String token) {
        String fromUsername = userService.getUsername(token, currentTransfer.getAccountFromId(), API_BASE_URL);
        String toUsername = userService.getUsername(token, currentTransfer.getAccountToId(), API_BASE_URL);
        String transferType = transferService.getTransferType(token, currentTransfer.getTransferId());
        String transferStatus = transferService.getTransferStatus(token, currentTransfer.getTransferId());
        consoleService.displayTransferDetails(currentTransfer, fromUsername, toUsername, transferType, transferStatus);
    }

    private void viewPendingRequests() {
        String token = currentUser.getToken();
        List<Transfer> pendingTransfers = transferService.getPendingTransfers(currentUser);

        if (pendingTransfers == null || pendingTransfers.isEmpty()) {
            consoleService.printColorLine("No pending requests found.", Colors.YELLOW);
            return;
        }

        consoleService.printPendingTransfersHeader();
        for (Transfer transfer : pendingTransfers) {
            String toUsername = userService.getUsername(token, transfer.getAccountToId(), API_BASE_URL);
            String transferAmountFormatted = currencyNumberFormat.format(transfer.getAmount());
            consoleService.printPendingTransfer(transfer.getTransferId(), toUsername, transferAmountFormatted);
        }

        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel):");
        if (transferId != 0) {
            processPendingTransfer(transferId, pendingTransfers);
        }
    }

    private void processPendingTransfer(int transferId, List<Transfer> pendingTransfers) {
        Transfer transfer = getTransferId(transferId, pendingTransfers);
        if (transfer == null) {
            consoleService.printColorLine("Invalid transfer ID.", Colors.RED);
            return;
        }

        int choice = consoleService.promptForMenuSelection("1: Approve\n2: Reject\n0: Cancel\nPlease choose an option: ");
        if (choice == 1 ) {
            boolean isCoverable = accountService.viewCurrentBalance(currentUser, API_BASE_URL).compareTo(transferService.getTransferDetails(currentUser, transferId).getAmount()) >= BigDecimal.ZERO.intValue();

            if(!isCoverable){
                String transferAmountFormatted = currencyNumberFormat.format(transfer.getAmount());
                String userBucksAmountFormatted = currencyNumberFormat.format(accountService.viewCurrentBalance(currentUser, API_BASE_URL));
                consoleService.printInsufficientFundsMessage(transferAmountFormatted,userBucksAmountFormatted);
                return;
            }

            boolean success = transferService.approveTransfer(currentUser, transferId);
            if (success && transferFunds(transfer)) {
                consoleService.printColorLine("Transfer approved successfully!", Colors.GREEN);
                consoleService.pause();
            } else {
                consoleService.printErrorMessage();
            }
        } else if (choice == 2) {
            boolean success = transferService.rejectTransfer(currentUser, transferId);
            if (success) {
                consoleService.printColorLine("Transfer rejected successfully!", Colors.GREEN);
            } else {
                consoleService.printErrorMessage();
            }
        } else if (choice == 0) {
            consoleService.printColorLine("Operation canceled.", Colors.YELLOW);
        } else {
            consoleService.printColorLine("Invalid selection.", Colors.RED);
        }
    }

    private boolean transferFunds(Transfer transfer) {
        boolean transferredFunds = false;
        //  Updating the account balances from user
        Account accountToUpdate = accountService.getAccount(currentUser, transfer.getAccountFromId(), API_BASE_URL);

        if(accountToUpdate == null) {
            consoleService.printErrorMessage();
            return transferredFunds;
        }
        accountToUpdate.setBalance( accountToUpdate.getBalance().subtract(transfer.getAmount()) );
        accountService.updateCurrentBalance(currentUser, accountToUpdate, API_BASE_URL);

        //  Updating the account balances to user
        accountToUpdate = accountService.getAccount(currentUser, transfer.getAccountToId(), API_BASE_URL);

        if(accountToUpdate == null) {
            consoleService.printErrorMessage();
            return transferredFunds;
        }

        accountToUpdate.setBalance( accountToUpdate.getBalance().add(transfer.getAmount()) );
        accountService.updateCurrentBalance(currentUser, accountToUpdate, API_BASE_URL);
        transferredFunds = true;

        return transferredFunds;
    }



    /**
     * This function compares given user id against a list of users,
     * return false when user id does not exist int the list
     * or when user id is same as the current logged in user
     * 
     * @param users the list of users to choose from
     * @param userId chosen user id
     * @return
     */
    private boolean wasValidUserSelected(List<User> users, int userId) {
        // ensure the selected user is not the same as teh current user
        if(userId==currentUser.getUser().getId())
            return false;

        for (User user : users) {
            if(user.getId()==userId) return true;
        }
        return false;
    }

    private void createTransfer(TransferTypes transferType) {
        //Get the list of all users
        List<User> users;
        users = userService.getAllUsers(currentUser, API_BASE_URL);

        //Prompt the user for the ID of the user they want to send to
        int toUserId = consoleService.promptForUserId(users, currentUser.getUser().getId(), transferType);
        
        if (toUserId == 0) {
            return; //Cancel the operation if the user entered 0
        }

        while(!wasValidUserSelected(users, toUserId))
        {
            consoleService.printColor("Invalid user ID " + toUserId, Colors.RED);
            toUserId = consoleService.promptForUserId(users, currentUser.getUser().getId(), transferType);
            if (toUserId == 0) return; //Cancel the operation if the user entered 0
        }

        //Prompt the user for the amount they want to send/request
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");

        //Perform the transfer
        
        Integer transferId = accountService.createTransfer(currentUser, API_BASE_URL, toUserId, amount, transferType);

        if (transferId>=0) {
            if(transferType ==TransferTypes.SEND) {
                consoleService.printColorLine("Money sent successfully! Transfer Id: " + transferId, Colors.GREEN);
            } else {
                consoleService.printColorLine("Request sent successfully and pending approval! Transfer Id: " + transferId, Colors.GREEN);
            }
            
        } else if(transferId==-2){
            
            consoleService.printBoxedError("Transfer failed:\n\tNot enough funds in the account.");
        }
        else {
            consoleService.printBoxedError("Transfer failed:\n\tPlease check the log for details.");
        }

    }
}
