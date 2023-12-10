import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Administrator {
    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\Yura\\Desktop\\user_credentials.txt";
        public static void vesp() {

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("ADMIN MENU");
                System.out.println("0. Exit");
                System.out.println("1. Create vendor account");
                System.out.println("2. Create customer account");
                System.out.println("3. Create runner account");
                System.out.println("4. Read vendor account");
                System.out.println("5. Read customer account");
                System.out.println("6. Read runner account");
                System.out.println("7. Update vendor account");
                System.out.println("8. Update customer account");
                System.out.println("9. Update runner account");
                System.out.println("10. Delete vendor account");
                System.out.println("11. Delete customer account");
                System.out.println("12. Delete runner account");
                System.out.println("13. Top-up customer credit");
                System.out.println("14. Generate transaction receipt");
                System.out.println("15. Send receipt to customer through notification");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createVendorAccount(scanner);
                        break;
                    case 2:
                        createCustomerAccount(scanner);
                        break;
                    case 3:
                        createRunnerAccount(scanner);
                        break;
                    case 4:
                        readVendorAccount();
                        break;
                    case 5:
                        readCustomerAccount();
                        break;
                    case 6:
                        readRunnerAccount();
                        break;
                    case 7:
                        updateVendorAccount(scanner);
                        break;
                    case 8:
                        updateCustomerAccount(scanner);
                        break;
                    case 9:
                        updateRunnerAccount(scanner);
                        break;
                    case 10:
                        deleteVendorAccount(scanner);
                        break;
                    case 11:
                        deleteCustomerAccount(scanner);
                        break;
                    case 12:
                        deleteRunnerAccount(scanner);
                        break;

                    case 0:
                        System.out.println("Exiting program...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;


                }

            }

        }

    ///// ACCOUNT CREATING //////////////////////////////////////////////////////////////////////////////////////////////
    public static void createVendorAccount(Scanner scanner) {
        System.out.println("Login,password,VendorID");
        scanner.nextLine();
        String vendorData = "vendor:" + scanner.nextLine();
        appendDataToFile(CREDENTIALS_FILE_PATH, vendorData);
    }

    public static void createCustomerAccount(Scanner scanner) {
        System.out.println("Login,password,CustomerID");
        scanner.nextLine();
        String customerData = "customer:" + scanner.nextLine();
        appendDataToFile(CREDENTIALS_FILE_PATH, customerData);
    }

    public static void createRunnerAccount(Scanner scanner) {
        System.out.println("Login,password,RunnerID");
        scanner.nextLine();
        String runnerData = "runner:" + scanner.nextLine();
        appendDataToFile(CREDENTIALS_FILE_PATH, runnerData);
    }
    private static void appendDataToFile(String fileName, String data) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(data + "\n");
            System.out.println("Account has been created.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    //////// ACCOUNT READING ///////////////////////////////////////////////////
    public static void readVendorAccount() {
        readFileAndPrintAccounts("vendor:");
    }

    public static void readCustomerAccount() {
        readFileAndPrintAccounts("customer:");
    }

    public static void readRunnerAccount() {
        readFileAndPrintAccounts("runner:");
    }

    private static void readFileAndPrintAccounts(String accountTypePrefix) {
        try {
            File credentialsFile = new File(CREDENTIALS_FILE_PATH);
            Scanner fileScanner = new Scanner(credentialsFile);
            int recordNumber = 1;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith(accountTypePrefix)) {
                    String[] data = line.substring(accountTypePrefix.length()).split(",");

                    System.out.print(recordNumber + ". ");
                    for (String item : data) {
                        System.out.print(item + " | ");
                    }
                    System.out.println();
                    recordNumber++;
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }


    ///////// ACCOUNT UPDATING ////////////////////////////////////////
    public static void updateVendorAccount(Scanner consoleScanner) {
        updateAccount(consoleScanner, "vendor:");
    }

    public static void updateCustomerAccount(Scanner consoleScanner) {
        updateAccount(consoleScanner, "customer:");
    }

    public static void updateRunnerAccount(Scanner consoleScanner) {
        updateAccount(consoleScanner, "runner:");
    }

    private static void updateAccount(Scanner consoleScanner, String accountTypePrefix) {
        List<String> accounts = new ArrayList<>();
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(credentialsFile);
            while (fileScanner.hasNextLine()) {
                accounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            int recordNumber = 1;
            for (String account : accounts) {
                if (account.startsWith(accountTypePrefix)) {
                    System.out.println(recordNumber + ". " + account.substring(accountTypePrefix.length()));
                    recordNumber++;
                }
            }

            System.out.println("Choose the number of account you wanna update:");
            int accountNumber = consoleScanner.nextInt() - 1;
            consoleScanner.nextLine();

            int index = 0;
            int accountIndexToUpdate = -1;
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).startsWith(accountTypePrefix)) {
                    if (index == accountNumber) {
                        accountIndexToUpdate = i;
                        break;
                    }
                    index++;
                }
            }

            if (accountIndexToUpdate != -1) {
                System.out.println("Enter new data:");
                String newData = consoleScanner.nextLine();
                accounts.set(accountIndexToUpdate, accountTypePrefix + newData);

                try (FileWriter fileWriter = new FileWriter(credentialsFile, false)) {
                    for (String account : accounts) {
                        fileWriter.write(account + "\n");
                    }
                    System.out.println("Account data was successfully updated.");
                } catch (IOException e) {
                    System.out.println("An error occurred while rewriting the file: " + e.getMessage());
                }
            } else {
                System.out.println("Incorrect account number!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    //// ACCOUNT DELETING //////////////////////////////////////////////////////////
    public static void deleteVendorAccount(Scanner scanner) {
        deleteAccount(scanner, "vendor:");
    }

    public static void deleteCustomerAccount(Scanner scanner) {
        deleteAccount(scanner, "customer:");
    }

    public static void deleteRunnerAccount(Scanner scanner) {
        deleteAccount(scanner, "runner:");
    }
    private static void deleteAccount(Scanner consoleScanner, String accountTypePrefix) {
        List<String> accounts = new ArrayList<>();
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(credentialsFile);
            while (fileScanner.hasNextLine()) {
                accounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            int recordNumber = 1;
            for (String account : accounts) {
                if (account.startsWith(accountTypePrefix)) {
                    System.out.println(recordNumber + ". " + account.substring(accountTypePrefix.length()));
                    recordNumber++;
                }
            }

            System.out.println("Choose the number of account you wanna delete:");
            int accountNumber = consoleScanner.nextInt() - 1;
            consoleScanner.nextLine();

            int index = 0;
            int accountIndexToDelete = -1;
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).startsWith(accountTypePrefix)) {
                    if (index == accountNumber) {
                        accountIndexToDelete = i;
                        break;
                    }
                    index++;
                }
            }

            if (accountIndexToDelete != -1) {
                accounts.remove(accountIndexToDelete);

                try (FileWriter fileWriter = new FileWriter(credentialsFile, false)) {
                    for (String account : accounts) {
                        fileWriter.write(account + "\n");
                    }
                    System.out.println("Account data was successfully deleted.");
                } catch (IOException e) {
                    System.out.println("An error occurred while rewriting the file: " + e.getMessage());
                }
            } else {
                System.out.println("Incorrect account number!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }




}





