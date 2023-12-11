import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {

    private static final String TASK_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer files\\c.Tasks.txt";
    private static final String MENU_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer files\\c.Menu.txt";
    private static final String ORDER_HISTORY_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer files\\c.OrderHistory.txt";
    private static final String WALLET_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer files\\c.Wallets.txt";
    private static final String REVIEWS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer files\\c.Reviews.txt";
    public static void Menu() {
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("Welcome to the Customer menu!");
            System.out.println("Choose options:");
            System.out.println("1. View menu");
            System.out.println("2. Read customer review");
            System.out.println("3. Place order");
            System.out.println("4. Cancel order");
            System.out.println("5. Check order status");
            System.out.println("6. Check order history");
            System.out.println("7. Check transaction history");
            System.out.println("8. Provide a review for each order");
            System.out.println("9. Reorder using order history");
            System.out.println("0. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    readReview();
                    break;
                case 3:
                    placeOrder();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    chkOrderStatus();
                    break;
                case 6:
                    chkOrderHistory();
                    break;
                case 7:
                    System.out.println("Enter your customer ID to check transaction history: ");
                    String customerId = scanner.nextLine();
                    chkTransHistory(customerId);
                    break;
                case 8:
                    System.out.println("Enter customer id : ");
                    String customerid = scanner.nextLine();
                    leaveReview(customerid);
                    break;
                case 9:
                    System.out.println("Enter customer id : ");
                    String customerID = scanner.nextLine();
                    reorder(customerID);
                    break;
                case 0:
                    System.out.println("Goobye!.");
                    return;
                default:
                    System.out.println("Incorret action, please try again.");
            }
        }
    }
    //Allows user to view menu
    private static void viewMenu() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) !=null){
                System.out.println(line);
            }
        }   catch (IOException e){
            e.printStackTrace();
        }
    }
    //Allows user to read customer review
    private static void readReview(){}
    //Allows user to place order
    public static void placeOrder() {
        Scanner scanner = new Scanner(System.in);
        Map<String, String[]> menu = loadMenu();

        System.out.println("Enter your customer ID: ");
        String customerId = scanner.nextLine();

        double currentBalance = getBalance(customerId);
        System.out.println("Balance: RM" + currentBalance);
        viewMenu();

        System.out.println("Enter ID of selected dish: ");
        String itemId = scanner.nextLine();

        if (!menu.containsKey(itemId)) {
            System.out.println("Chosen ID hadn`t been found.");
            return;
        }

        String[] itemDetails = menu.get(itemId);
        String vendorName = itemDetails[0];
        String itemName = itemDetails[1];
        double price = Double.parseDouble(itemDetails[2]);

        if (currentBalance < price) {
            System.out.println("Not enough money to place the order.");
            return;
        }

        System.out.println("Enter date : (format MM/DD/YYYY): ");
        String orderDate = scanner.nextLine();

        System.out.println("Enter pickup method : ");
        System.out.println("Dine-In\nTake away\nDelivery");
        String pickUpMethod = scanner.nextLine();

        String orderStatus = "pending acceptance";
        String orderId = generateRandomOrderId();



        String orderDetails = orderId + ", " + vendorName + ", " + customerId + ", " + itemId + ", " + itemName + ", " + orderDate + ", RM" + (price) + ", " + pickUpMethod + ", " + orderStatus;


        double newBalance = currentBalance - price;
        updateBalance(customerId, newBalance);
        recordTransaction(customerId, -price, "Order placed: " + itemName);

        try {

            writeToFiles(orderDetails);
            System.out.println("Order has been successfully recorded to Order History: " + orderDetails);
        } catch (IOException e) {
            System.out.println("An error occurred recording order history.");
        }
    }

    private static String generateRandomOrderId() {
        Random random = new Random();
        int length = random.nextInt(6) + 1;
        StringBuilder orderId = new StringBuilder();
        for (int i = 0; i < length; i++) {
            orderId.append(random.nextInt(10));
        }
        return orderId.toString();
    }

    private static void writeToFiles(String orderDetails) throws IOException {
        String orderId = generateRandomOrderId();
        // Write to Task.txt
        BufferedWriter taskWriter = new BufferedWriter(new FileWriter(TASK_FILE_PATH, true));
        taskWriter.write(orderDetails + "\n");
        taskWriter.close();

        // Write to c.OrderHistory.txt
        BufferedWriter historyWriter = new BufferedWriter(new FileWriter(ORDER_HISTORY_FILE_PATH, true));
        historyWriter.write(orderId + ", " + orderDetails + "\n");
        historyWriter.close();
    }


    private static double getBalance(String customerId) {
        File walletsFile = new File(WALLET_FILE_PATH);
        double balance = 0.0;

        try {
            Scanner scanner = new Scanner(walletsFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Assuming the format is: customerId;balance;transactionHistory
                String[] parts = line.split(";");
                if (parts[0].equals(customerId) && parts.length > 1) {
                    balance = Double.parseDouble(parts[1]);
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File wallets.txt not found.");
        } catch (NumberFormatException e) {
            System.out.println("Error transforming balance to number.");
        }

        return balance;
    }
    private static void recordTransaction(String customerId, double amount, String description) {
        File walletsFile = new File(WALLET_FILE_PATH);
        File tempFile = new File(walletsFile.getAbsolutePath() + ".tmp");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(walletsFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            boolean found = false;

            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";");
                if (parts[0].equals(customerId) && parts.length > 1) {
                    // Append the new transaction to the transaction history
                    String transactionRecord = description + " (RM" + amount + ")";
                    String newTransactionHistory = parts.length > 2 ? parts[2] + "," + transactionRecord : transactionRecord;
                    String updatedLine = customerId + ";" + parts[1] + ";" + newTransactionHistory;
                    writer.write(updatedLine + System.lineSeparator());
                    found = true;
                } else {
                    // Write the line as is if it's not the matching customer ID
                    writer.write(currentLine + System.lineSeparator());
                }
            }

            if (!found) {
                // If the customer ID wasn't found, create a new entry with the transaction
                String transactionRecord = description + " (RM" + amount + ")";
                writer.write(customerId + ";" + amount + ";" + transactionRecord + "\n");
            }

            writer.close();
            reader.close();

            // Replace the original file with the updated file
            if (!walletsFile.delete()) {
                System.out.println("Can`t delete original file.");
                return;
            }

            if (!tempFile.renameTo(walletsFile)) {
                System.out.println("An error updating wallets file.");
            }

        } catch (IOException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
        }
    }

    private static void updateBalance(String customerId, double newBalance) {
        File walletsFile = new File(WALLET_FILE_PATH);
        File tempFile = new File(walletsFile.getAbsolutePath() + ".tmp");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(walletsFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            boolean found = false;

            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(";");
                if (parts[0].equals(customerId) && parts.length > 1) {
                    // Update the balance for the matching customer ID
                    String updatedLine = customerId + ";" + newBalance + ";" + (parts.length > 2 ? parts[2] : "");
                    writer.write(updatedLine + System.lineSeparator());
                    found = true;
                } else {
                    // Write the line as is if it's not the matching customer ID
                    writer.write(currentLine + System.lineSeparator());
                }
            }

            if (!found) {
                // If the customer ID wasn't found, add a new entry
                writer.write(customerId + ";" + newBalance + ";\n");
            }

            writer.close();
            reader.close();

            // Replace the original file with the updated file
            if (!walletsFile.delete()) {
                System.out.println("Error updating original file.");
                return;
            }

            if (!tempFile.renameTo(walletsFile)) {
                System.out.println("Error updating balance file.");
            }

        } catch (IOException e) {
            System.out.println("Error updating balance file: " + e.getMessage());
        }
    }



    public static Map<String, String[]> loadMenu() {
        Map<String, String[]> menu = new HashMap<>();
        try {
            File menuFile = new File(MENU_FILE_PATH);
            Scanner fileScanner = new Scanner(menuFile);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    // Format: itemID, vendorName, dishName, price
                    String itemId = parts[0].trim();
                    String vendorName = parts[1].trim();
                    String dishName = parts[2].trim();
                    String price = parts[3].trim();
                    menu.put(itemId, new String[]{vendorName, dishName, price});
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Menu file not found.");
        }
        return menu;
    }


    //Allows user to cancel order, if any
    public static void cancelOrder() {
        try {
            File taskFile = new File(TASK_FILE_PATH);
            File tempFile = new File(taskFile.getAbsolutePath() + ".tmp");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter ID of order to cancel: ");
            String orderId = scanner.nextLine();

            BufferedReader reader = new BufferedReader(new FileReader(taskFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            boolean found = false;

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.contains(orderId)) {
                    found = true;
                    continue; // Skip the line that matches the order ID
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }


            writer.close();
            reader.close();


            if (!found) {
                System.out.println("Order with this ID hasn`t been found.");
                if (!tempFile.delete()) {
                    System.out.println("Error deleting temporary file.");
                }
                return;
            }

            // Delete the original file and rename the temp file to the original file name
            if (!taskFile.delete()) {
                System.out.println("Error deleting original order file.");
                return;
            }


            if (!tempFile.renameTo(taskFile)) {
                System.out.println("Error renaming temporary file to original file.");
            } else {
                System.out.println("Order successfully canceled.");
            }

        } catch (IOException ex) {
            System.out.println("Error processing file: " + ex.getMessage());
        }
    }
    //Allows user to check order status, if any
    public static void chkOrderStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ID of order to check status: ");
        String orderId = scanner.nextLine();

        try {
            File taskFile = new File(TASK_FILE_PATH);
            Scanner fileScanner = new Scanner(taskFile);
            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Assuming the order format is: itemName, customerId, orderDate, price, status
                if (line.contains(orderId)) {
                    String[] orderDetails = line.split(", ");
                    if (orderDetails.length >= 5) {
                        String status = orderDetails[6];
                        System.out.println("Order Status: " + status);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("Order with this ID hasn`t been found.");
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }
    //Allows user to check personal ordering history
    public static void chkOrderHistory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter customer ID to check order history: ");
        String customerId = scanner.nextLine();

        try {
            File historyFile = new File(ORDER_HISTORY_FILE_PATH);
            Scanner fileScanner = new Scanner(historyFile);
            boolean hasOrders = false;

            System.out.println("Order history for customer: " + customerId);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                // Assuming the order format is: itemName, customerId, orderDate, price, status
                if (line.contains(customerId)) {
                    System.out.println(line);
                    hasOrders = true;
                }
            }

            if (!hasOrders) {
                System.out.println("Order history unavailable.");
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Order history file not found.");
        }
    }
    //Allows user to check transaction history
    public static void chkTransHistory(String customerId) {
        try {
            File walletsFile = new File(WALLET_FILE_PATH);
            Scanner scanner = new Scanner(walletsFile);
            boolean found = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(customerId + ";")) {
                    System.out.println("Transaction history for " + customerId + ":");
                    System.out.println(line.substring(line.indexOf(";") + 1)); // Assuming transactions are stored after a semicolon
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Order history for this customer hasn`t been found.");
                createWalletForNewCustomer();
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File wallets.txt not found.");
        }
    }
    public static void createWalletForNewCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your customer ID to create new wallet: ");
        String customerId = scanner.nextLine();

        // Call the function to create a new wallet record
        createNewWalletRecord(customerId);
    }
    public static void createNewWalletRecord(String customerId) {
        File walletsFile = new File(WALLET_FILE_PATH);
        double initialBalance = 100.0; // Standard initial balance

        try {
            // Check if the customer already exists
            if (doesCustomerExist(customerId)) {
                System.out.println("Wallet record already exists for this customer.");
                return;
            }

            // Append the new customer record to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(walletsFile, true));
            String newRecord = customerId + ";" + initialBalance + ";"; // Empty transaction history
            writer.write(newRecord + System.lineSeparator());
            writer.close();

            System.out.println("New wallet record created for customer: " + customerId);
        } catch (IOException e) {
            System.out.println("Error creating wallet record: " + e.getMessage());
        }
    }

    private static boolean doesCustomerExist(String customerId) throws IOException {
        File walletsFile = new File(WALLET_FILE_PATH);
        Scanner scanner = new Scanner(walletsFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith(customerId + ";")) {
                scanner.close();
                return true;
            }
        }

        scanner.close();
        return false;
    }


    //Allows user to provide a review for each order
    public static void leaveReview(String customerId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the ID of the order you want to review:");
        String orderId = scanner.nextLine();

        if (!doesOrderExist(orderId, customerId)) {
            System.out.println("Order not found or does not belong to this customer.");
            return;
        }
        System.out.println("Enter your name :");
        String customerName = scanner.nextLine();
        System.out.println("Enter your review:");
        String reviewText = scanner.nextLine();


        saveReview(orderId, customerId, reviewText, customerName);
    }

    private static boolean doesOrderExist(String orderId, String customerId) {
        File orderHistoryFile = new File(ORDER_HISTORY_FILE_PATH);
        try {
            Scanner scanner = new Scanner(orderHistoryFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(orderId + ",") && line.contains(", " + customerId + ",")) {
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading order history file: " + e.getMessage());
        }
        return false;
    }

    public static void saveReview(String orderId, String customerId, String reviewText, String customerName) {
        String courierId = getCourierIdForOrder(customerId);

        if (courierId == null) {
            System.out.println("No courier found for the given order.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEWS_FILE_PATH, true))) {
            writer.write(orderId + "/ " + customerId + "/ " + customerName + "/ " + reviewText + "/ " + courierId + "\n");
            System.out.println("Review saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing review: " + e.getMessage());
        }
    }

    private static String getCourierIdForOrder(String customerId) {
        String COURIER_TASKS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer files\\c.CourierTasks.txt";
        File courierTasksFile = new File(COURIER_TASKS_FILE_PATH);
        try (Scanner scanner = new Scanner(courierTasksFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length > 5 && parts[1].equals(customerId)) {
                    return parts[5]; // Assuming the 6th column is the courier ID
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courier tasks file: " + e.getMessage());
        }
        return null;
    }

    // Example usage
    //Allows user to reorder same ordered items from history
    public static void reorder(String customerId) {
        List<String> orderHistory = getCustomerOrderHistory(customerId);

        if (orderHistory.isEmpty()) {
            System.out.println("No previous orders found for customer ID: " + customerId);
            return;
        }

        System.out.println("Previous orders for customer ID " + customerId + ":");
        for (int i = 0; i < orderHistory.size(); i++) {
            System.out.println((i + 1) + ": " + orderHistory.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of the order you wish to repeat:");
        int orderChoice;
        try {
            orderChoice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Operation cancelled.");
            return;
        }

        if (orderChoice < 1 || orderChoice > orderHistory.size()) {
            System.out.println("Invalid order number. Operation cancelled.");
            return;
        }

        // Process the selected order
        String selectedOrder = orderHistory.get(orderChoice - 1);
        placeOrderAgain(selectedOrder);
    }

    private static List<String> getCustomerOrderHistory(String customerId) {
        List<String> orderHistory = new ArrayList<>();
        File orderHistoryFile = new File(ORDER_HISTORY_FILE_PATH);

        try {
            Scanner scanner = new Scanner(orderHistoryFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(customerId)) {
                    orderHistory.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Order history file not found.");
        }

        return orderHistory;
    }

    public static void placeOrderAgain(String orderDetails) {
        // Assuming orderDetails format is: orderId, vendorName, customerId, itemId, itemName, orderDate, price, orderStatus
        String[] parts = orderDetails.split(", ");
        if (parts.length < 8) {
            System.out.println("Invalid order details.");
            return;
        }
        
        String newOrderId = generateRandomOrderId(); // Generate a new order ID for the repeated order
        String customerId = parts[2];
        String itemId = parts[3];
        String itemName = parts[4];
        String orderDate = parts[5]; // You might want to set this to the current date instead
        String price = parts[6];
        String orderStatus = "pending acceptance"; // Setting new order status

        String newOrderDetails = newOrderId + ", " + parts[1] + ", " + customerId + ", " + itemId + ", " + itemName + ", " + orderDate + ", " + price + ", " + orderStatus;

        try {
            // Write the new order details to Task.txt and OrderHistory.txt
            writeToFiles(newOrderDetails);
            System.out.println("Order placed again successfully: " + newOrderDetails);
        } catch (IOException e) {
            System.out.println("Error placing order again: " + e.getMessage());
        }
    }
}
