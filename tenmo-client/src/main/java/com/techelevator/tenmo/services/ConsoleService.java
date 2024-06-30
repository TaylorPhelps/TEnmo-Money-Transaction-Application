package com.techelevator.tenmo.services;

import com.techelevator.tenmo.TransferTypes;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    public enum Colors {
        RESET,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        CYAN;
        public String toString(){
            switch (this) {
                case RESET: return "\u001B[0m";
                case BLUE: return "\u001B[34m";
                case RED: return "\u001B[31m";
                case GREEN: return "\u001B[32m";
                case YELLOW: return"\033[0;33m"; 
                case CYAN: return "\033[0;36m"; 
                default: return null;
            }
        }
    }

    private final String LINE = "-------------------------------------------";
    private final String ERROR_LINE = "****************************************";

    private final Scanner scanner = new Scanner(System.in);

    public String padRight(String s, int n) {
        if(n<1){
            return s;
        }
        return String.format("%-" + n + "s", s);  
    }
    public String padLeft(String s, int n) {
        if(n<1){
            return s;
        }
        return String.format("%" + n + "s", s);  
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Logoff");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printCurrentBalance(String balance){
        System.out.println("Your current account balance is: " +  balance);
    }

    public void printTransferMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID\tFrom/To\t\tAmount");
        System.out.println("-------------------------------------------");
    }

    public void printTransferOverview(Integer transferId, String transferType, String username, String transferAmount) {
        System.out.println(transferId + "\t" + transferType + ": " + username + "\t" + transferAmount);
    }

    public void displayTransferDetails(Transfer transfer, String sender, String receiver, String transferType, String transferStatus) {
        if (transfer == null) {
            System.out.println("Transfer details not available.");
            return;
        }

        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println(" Id: " + transfer.getTransferId());
        System.out.println(" From: " + sender);
        System.out.println(" To: " + receiver);
        System.out.println(" Type: " + transferType);
        System.out.println(" Status: " + transferStatus);
        System.out.println(" Amount: $" + transfer.getAmount());
    }
    public void printPendingTransfersHeader() {
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To                 Amount");
        System.out.println("-------------------------------------------");
    }

    public void printPendingTransfer(int transferId, String toUsername, String amount) {
        System.out.printf("%-10d %-10s %s%n", transferId, toUsername, amount);
    }


    public int promptForUserId(List<User> users, Integer currentUserId, TransferTypes transferType){
        printColor(LINE, Colors.BLUE);
        System.out.println("Users");
        printColor(padRight("ID", 4) + padRight("Name", 6), Colors.CYAN);
        printColor(LINE, Colors.YELLOW);
        for(User user : users){
            if(user.getId() != currentUserId)
                System.out.println(user.getId() + " " + user.getUsername());
        }
        printColor(LINE, Colors.YELLOW);
        if(transferType == TransferTypes.SEND)
            return promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        else 
            return promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
    }

    public void printRequestBucksMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID\t\t\t\tName");
        System.out.println("-------------------------------------------");
    }

    public BigDecimal printRequestAmountMenu(){
        BigDecimal requestAmount = promptForBigDecimal("Enter Amount: ");
        while(requestAmount.compareTo(BigDecimal.ZERO) <= 0){
            requestAmount = promptForBigDecimal("Please enter a valid amount: ");
        }

        return requestAmount;
    }

    public void printColorLine(String string, Colors color){
        System.out.println(color.toString() + string + Colors.RESET.toString());
    }
    
    public void printColor(String string, Colors color){
        System.out.println(color.toString() + string + Colors.RESET.toString());
    }

    public void printBoxedError(String message){
        printColorLine(ERROR_LINE,Colors.RED);
        String[] lines = message.split("\n");
        for (String line : lines) {
            printColorLine(
            padLeft(line, (ERROR_LINE.length()-line.length())/2),
            Colors.RED);    
        }
        
        printColorLine(ERROR_LINE,Colors.RED);
    }

    public void printInsufficientFundsMessage(String amountToTransfer, String userCurrentBalance){
        String builder = "Your balance of " + userCurrentBalance + " is less than the transfer request of " + amountToTransfer + "." +
                "\nPlease ensure to have the appropriate amount of bucks and try again.";
        printBoxedError(builder);
        pause();
    }
}
