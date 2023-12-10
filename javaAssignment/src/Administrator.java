import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class Administrator {
    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\Dimash\\Desktop\\user_credentials.txt";
    public static void vesp() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<ADMIN MENU>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("1. Create/Update/Delete/Read: vendor account");
            System.out.println("2. Create/Update/Delete/Read: customer account");
            System.out.println("3. Create/Update/Delete/Read: runner account");
            System.out.println("4. Top-up customer credit");
            System.out.println("5. Generate transaction receipt");
            System.out.println("6. Send receipt to customer through notification");
            System.out.println("0. Exit");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    vendorEditMenu();
                    break;
                case 2:
                    customerEditMenu();
                    break;
                case 3:
                    runnerEditMenu();
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;


            }

        }

    }

    private static void  vendorEditMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Vendor Edit Menu:");
            System.out.println("1. Create vendor account");
            System.out.println("2. Update vendor account");
            System.out.println("3. Delete vendor account");
            System.out.println("4. Read vendor account");
            System.out.println("0. Exit");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    registerUser("vendor");
                    break;
                case 2:
                    updateVendorAccount(scanner);
                    break;
                case 3:
                    deleteVendorAccount(scanner);
                    break;
                case 4:
                    readVendorAccount();
                    break;
                case 0:
                    System.out.println("Exiting menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void  customerEditMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Customer Edit Menu:");
            System.out.println("1. Create customer account");
            System.out.println("2. Update customer account");
            System.out.println("3. Delete customer account");
            System.out.println("4. Read customer account");
            System.out.println("0. Exit");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    registerUser("customer");
                    break;
                case 2:
                    updateCustomerAccount(scanner);
                    break;
                case 3:
                    deleteCustomerAccount(scanner);
                    break;
                case 4:
                    readCustomerAccount();
                    break;
                case 0:
                    System.out.println("Exiting menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void  runnerEditMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Runner Edit Menu:");
            System.out.println("1. Create Delivery account");
            System.out.println("2. Update Delivery account");
            System.out.println("3. Delete Delivery account");
            System.out.println("4. Read Delivery account");
            System.out.println("0. Exit");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    registerUser("delivery");
                    break;
                case 2:
                    updateRunnerAccount(scanner);
                    break;
                case 3:
                    deleteRunnerAccount(scanner);
                    break;
                case 4:
                    readRunnerAccount();
                    break;
                case 0:
                    System.out.println("Exiting menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    ///// ACCOUNT CREATING //////////////////////////////////////////////////////////////////////////////////////////////

    private static void registerUser(String role) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username for " + role + ": ");
        String username = scanner.nextLine();
        System.out.print("Enter password for " + role + ": ");
        String password = scanner.nextLine();

        try (FileWriter fw = new FileWriter(CREDENTIALS_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(role + "," + username + "," + password);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }

        System.out.println(role + " user registered successfully.");
    }

    //////// ACCOUNT READING ///////////////////////////////////////////////////
    public static void readVendorAccount() {
        readFileAndPrintAccounts("vendor,");
    }

    public static void readCustomerAccount() {
        readFileAndPrintAccounts("customer,");
    }

    public static void readRunnerAccount() {readFileAndPrintAccounts("delivery,");}

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
        updateAccount(consoleScanner, "vendor,");
    }

    public static void updateCustomerAccount(Scanner consoleScanner) {
        updateAccount(consoleScanner, "customer,");
    }

    public static void updateRunnerAccount(Scanner consoleScanner) {
        updateAccount(consoleScanner, "delivery,");
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
        deleteAccount(scanner, "vendor,");
    }

    public static void deleteCustomerAccount(Scanner scanner) {
        deleteAccount(scanner, "customer,");
    }

    public static void deleteRunnerAccount(Scanner scanner) {
        deleteAccount(scanner, "delivery,");
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
