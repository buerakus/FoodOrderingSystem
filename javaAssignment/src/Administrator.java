import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class Admin {
    private static final String CUSTOMERS_FILE_PATH = "C:\\Users\\Yura\\Desktop\\Customers.txt";
    private static final String VENDORS_FILE_PATH = "C:\\Users\\Yura\\Desktop\\Vendors.txt";
    private static final String RUNNERS_FILE_PATH = "C:\\Users\\Yura\\Desktop\\Runners.txt";


    public static void yura() {

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
    // ACCOUNT CREATING --------------------------------------------------------------------------

    public static void createCustomerAccount(Scanner scanner) {
        System.out.println("Name,Login,password,WalletID");
        scanner.nextLine();
        String customerData = scanner.nextLine();
        appendCustomerDataToFile(CUSTOMERS_FILE_PATH, customerData);
    }

    private static void appendCustomerDataToFile(String fileName, String data) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(data + "\n");
            System.out.println("Customer account has been created.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static void createVendorAccount(Scanner scanner) {
        System.out.println("Name,Login,password,VendorID");
        scanner.nextLine();
        String vendorData = scanner.nextLine();
        appendVendorDataToFile(VENDORS_FILE_PATH, vendorData);
    }

    private static void appendVendorDataToFile(String fileName, String data) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(data + "\n");
            System.out.println("Vendor account has been created.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static void createRunnerAccount(Scanner scanner) {
        System.out.println("Name,Login,password,RunnerID");
        scanner.nextLine();
        String runnerData = scanner.nextLine();
        appendVendorDataToFile(RUNNERS_FILE_PATH, runnerData);
    }

    private static void appendRunnerDataToFile(String fileName, String data) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(data + "\n");
            System.out.println("Runner account has been created.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    // ACCOUNT READING -------------------------------------------------------------------------------
    public static void readVendorAccount() {
        try {
            File vendorsFile = new File(VENDORS_FILE_PATH);
            Scanner fileScanner = new Scanner(vendorsFile);
            int recordNumber = 1;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                System.out.println(recordNumber + ". ");
                for (String item : data) {
                    System.out.print(item + " | ");
                }
                System.out.println();
                recordNumber++;
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public static void readCustomerAccount() {
        try {
            File customersFile = new File(CUSTOMERS_FILE_PATH);
            Scanner fileScanner = new Scanner(customersFile);
            int recordNumber = 1;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                System.out.println(recordNumber + ". ");
                for (String item : data) {
                    System.out.print(item + " | ");
                }
                System.out.println();
                recordNumber++;
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public static void readRunnerAccount() {
        try {
            File runnersFile = new File(RUNNERS_FILE_PATH);
            Scanner fileScanner = new Scanner(runnersFile);
            int recordNumber = 1;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");

                System.out.println(recordNumber + ". ");
                for (String item : data) {
                    System.out.print(item + " | ");
                }
                System.out.println();
                recordNumber++;
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    // ACCOUNT UPDATING ------------------------------------------------------------------------------------
    public static void updateVendorAccount(Scanner consoleScanner) {
        List<String> vendorAccounts = new ArrayList<>();
        File vendorsFile = new File(VENDORS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(vendorsFile);
            while (fileScanner.hasNextLine()) {
                vendorAccounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            for (int i = 0; i < vendorAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + vendorAccounts.get(i));
            }
            System.out.println("Choose the number of account you wanna update:");
            int accountNumber = consoleScanner.nextInt() - 1;
            consoleScanner.nextLine();

            if (accountNumber >= 0 && accountNumber < vendorAccounts.size()) {
                System.out.println("Enter new data:");
                String newData = consoleScanner.nextLine();
                vendorAccounts.set(accountNumber, newData);
                try (FileWriter fileWriter = new FileWriter(vendorsFile, false)) {
                    for (String account : vendorAccounts) {
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

    public static void updateCustomerAccount(Scanner consoleScanner) {
        List<String> customerAccounts = new ArrayList<>();
        File customersFile = new File(CUSTOMERS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(customersFile);
            while (fileScanner.hasNextLine()) {
                customerAccounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            for (int i = 0; i < customerAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + customerAccounts.get(i));
            }
            System.out.println("Choose the number of account you wanna update:");
            int accountNumber = consoleScanner.nextInt() - 1;
            consoleScanner.nextLine();

            if (accountNumber >= 0 && accountNumber < customerAccounts.size()) {
                System.out.println("Enter new data:");
                String newData = consoleScanner.nextLine();
                customerAccounts.set(accountNumber, newData);
                try (FileWriter fileWriter = new FileWriter(customersFile, false)) {
                    for (String account : customerAccounts) {
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

    public static void updateRunnerAccount(Scanner consoleScanner) {
        List<String> runnerAccounts = new ArrayList<>();
        File runnersFile = new File(RUNNERS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(runnersFile);
            while (fileScanner.hasNextLine()) {
                runnerAccounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            for (int i = 0; i < runnerAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + runnerAccounts.get(i));
            }
            System.out.println("Choose the number of account you wanna update:");
            int accountNumber = consoleScanner.nextInt() - 1;
            consoleScanner.nextLine();

            if (accountNumber >= 0 && accountNumber < runnerAccounts.size()) {
                System.out.println("Enter new data:");
                String newData = consoleScanner.nextLine();
                runnerAccounts.set(accountNumber, newData);
                try (FileWriter fileWriter = new FileWriter(runnersFile, false)) {
                    for (String account : runnerAccounts) {
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

    // ACCOUNT DELETING -----------------------------------------
    public static void deleteVendorAccount(Scanner consoleScanner) {
        List<String> vendorAccounts = new ArrayList<>();
        File vendorsFile = new File(VENDORS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(vendorsFile);
            while (fileScanner.hasNextLine()) {
                vendorAccounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            for (int i = 0; i < vendorAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + vendorAccounts.get(i));
            }

            System.out.println("Choose the number of account you wanna delete:");
            int accountNumber = consoleScanner.nextInt() - 1;

            if (accountNumber >= 0 && accountNumber < vendorAccounts.size()) {
                vendorAccounts.remove(accountNumber);

                try (FileWriter fileWriter = new FileWriter(vendorsFile, false)) {
                    for (String account : vendorAccounts) {
                        fileWriter.write(account + "\n");
                    }
                    System.out.println("Account data was successfully deleted.");
                } catch (IOException e) {
                    System.out.println("An error occurred while rewriting the file: " + e.getMessage());
                }
            } else {
                System.out.println("Incorect number of account.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }


    public static void deleteCustomerAccount(Scanner consoleScanner) {
        List<String> customerAccounts = new ArrayList<>();
        File customersFile = new File(CUSTOMERS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(customersFile);
            while (fileScanner.hasNextLine()) {
                customerAccounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            for (int i = 0; i < customerAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + customerAccounts.get(i));
            }

            System.out.println("Choose the number of account you wanna delete:");
            int accountNumber = consoleScanner.nextInt() - 1;

            if (accountNumber >= 0 && accountNumber < customerAccounts.size()) {
                customerAccounts.remove(accountNumber);

                try (FileWriter fileWriter = new FileWriter(customersFile, false)) {
                    for (String account : customerAccounts) {
                        fileWriter.write(account + "\n");
                    }
                    System.out.println("Account data was successfully deleted.");
                } catch (IOException e) {
                    System.out.println("An error occurred while rewriting the file: " + e.getMessage());
                }
            } else {
                System.out.println("Incorect number of account.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public static void deleteRunnerAccount(Scanner consoleScanner) {
        List<String> runnerAccounts = new ArrayList<>();
        File runnersFile = new File(RUNNERS_FILE_PATH);

        try {
            Scanner fileScanner = new Scanner(runnersFile);
            while (fileScanner.hasNextLine()) {
                runnerAccounts.add(fileScanner.nextLine());
            }
            fileScanner.close();

            for (int i = 0; i < runnerAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + runnerAccounts.get(i));
            }

            System.out.println("Choose the number of account you wanna delete:");
            int accountNumber = consoleScanner.nextInt() - 1;

            if (accountNumber >= 0 && accountNumber < runnerAccounts.size()) {
                runnerAccounts.remove(accountNumber);

                try (FileWriter fileWriter = new FileWriter(runnersFile, false)) {
                    for (String account : runnerAccounts) {
                        fileWriter.write(account + "\n");
                    }
                    System.out.println("Account data was successfully deleted.");
                } catch (IOException e) {
                    System.out.println("An error occurred while rewriting the file: " + e.getMessage());
                }
            } else {
                System.out.println("Incorect number of account.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }



}


