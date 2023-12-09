import java.util.Scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;

import java.io.File;
import java.io.*;



public class Customer {
    private static final String TASK_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";

    public static void Menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choice = scanner.nextInt();

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
                    chkTransHistory();
                    break;
                case 8:
                    reviewEachOrder();
                    break;
                case 9:
                    reorder();
                    break;
                case 0:
                    System.out.println("Программа завершена.");
                    return;
                default:
                    System.out.println("Выбрано некорректное действие. Пожалуйста, выберите снова.");
            }
        }
    }
    //Allows user to view menu
    private static void viewMenu() {
        String ORDERS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\orders.txt";
        try {
            File ordersFile = new File(ORDERS_FILE_PATH);

            if (ordersFile.exists()) {
                Scanner ordersScanner = new Scanner(ordersFile);

                System.out.println("Current Menu:");

                while (ordersScanner.hasNextLine()) {
                    String order = ordersScanner.nextLine();
                    System.out.println(order);
                }

                ordersScanner.close();
            } else {
                System.out.println("No orders available.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found.");

        }
    }
    //Allows user to read customer review
    private static void readReview(){

    }
    //Allows user to place order
    private static void placeOrder(){
        String CUSTOMER_ORDERS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer_orders.txt";
        String TASKS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
        String MENU_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\menu.txt";
        try {
            // Display options to the customer
            System.out.println("Enter the number of the dish you want to order: ");

            // Take user input for the chosen option
            Scanner scanner = new Scanner(System.in);
            int selectedDishNumber = scanner.nextInt();

            // Read the menu to get the selected dish
            File menuFile = new File(MENU_FILE_PATH);
            Scanner menuScanner = new Scanner(menuFile);

            int dishCounter = 1;
            String selectedDish = null;

            while (menuScanner.hasNextLine()) {
                selectedDish = menuScanner.nextLine();
                if (dishCounter == selectedDishNumber) {
                    break;
                }
                dishCounter++;
            }

            menuScanner.close();

            // Write the selected dish to the customer's order file
            FileWriter orderFileWriter = new FileWriter(CUSTOMER_ORDERS_FILE_PATH, true);
            BufferedWriter bufferedWriter = new BufferedWriter(orderFileWriter);
            bufferedWriter.write(selectedDish + "\n");
            bufferedWriter.close();

            // Write the selected dish to the tasks file
            FileWriter tasksFileWriter = new FileWriter(TASKS_FILE_PATH, true);
            BufferedWriter tasksBufferedWriter = new BufferedWriter(tasksFileWriter);
            tasksBufferedWriter.write(selectedDish + "\n");
            tasksBufferedWriter.close();

            System.out.println("Order placed successfully.");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());

        }


    }
    //Allows user to cancel order, if any
    private static void cancelOrder(){
        String ORDERS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
        try {
            File ordersFile = new File(ORDERS_FILE_PATH);

            if (ordersFile.exists()) {
                // Display existing orders to the customer
                System.out.println("Existing Orders:");

                Scanner orderScanner = new Scanner(ordersFile);

                int orderNumber = 1;

                while (orderScanner.hasNextLine()) {
                    String order = orderScanner.nextLine();
                    System.out.println(orderNumber + ". " + order);
                    orderNumber++;
                }

                // Ask the customer to enter the number of the order to cancel
                System.out.println("Enter the number of the order you want to cancel: ");
                Scanner scanner = new Scanner(System.in);
                int selectedOrderNumber = scanner.nextInt();

                // Remove the selected order from the orders file
                File tempFile = new File("temp.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                Scanner orderScanner2 = new Scanner(ordersFile);
                int orderCounter = 1;

                while (orderScanner2.hasNextLine()) {
                    String order = orderScanner2.nextLine();
                    if (orderCounter != selectedOrderNumber) {
                        writer.write(order + "\n");
                    }
                    orderCounter++;
                }

                writer.close();
                orderScanner2.close();

                // Replace the original file with the temporary file
                ordersFile.delete();
                tempFile.renameTo(ordersFile);

                System.out.println("Order canceled successfully.");
            } else {
                System.out.println("No existing orders to cancel.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    //Allows user to check order status, if any
    private static void chkOrderStatus() {
        String ORDERS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
        try {
            File ordersFile = new File(ORDERS_FILE_PATH);

            if (ordersFile.exists()) {
                // Display existing orders to the customer
                System.out.println("Existing Orders:");

                Scanner orderScanner = new Scanner(ordersFile);

                int orderNumber = 1;

                while (orderScanner.hasNextLine()) {
                    String order = orderScanner.nextLine();
                    System.out.println(orderNumber + ". " + order);
                    orderNumber++;
                }

                // Ask the customer to enter the number of the order to check status
                System.out.println("Enter the number of the order to check its status: ");
                Scanner scanner = new Scanner(System.in);
                int selectedOrderNumber = scanner.nextInt();

                // Check the status of the selected order
                String orderStatus = getOrderStatus(selectedOrderNumber);
                if (orderStatus != null) {
                    System.out.println("Order Status: " + orderStatus);
                } else {
                    System.out.println("Invalid order number. Please try again.");
                }

                orderScanner.close();
            } else {
                System.out.println("No existing orders to check.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static String getOrderStatus(int selectedOrderNumber) {
        String ORDERS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
        try {
            File ordersFile = new File(ORDERS_FILE_PATH);
            Scanner orderScanner = new Scanner(ordersFile);

            int orderCounter = 1;

            while (orderScanner.hasNextLine()) {
                String order = orderScanner.nextLine();
                if (orderCounter == selectedOrderNumber) {
                    // Assuming the order status is stored as the last element in the order details
                    String[] orderDetails = order.split(",");
                    if (orderDetails.length > 0) {
                        return orderDetails[orderDetails.length - 1].trim();
                    }
                }
                orderCounter++;
            }

            orderScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found.");
        }

        return null;
    }

    //Allows user to check personal ordering history
    private static void chkOrderHistory() {
        String ORDERS_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
        try {
            File ordersFile = new File(ORDERS_FILE_PATH);

            if (ordersFile.exists()) {
                // Display order history to the customer
                System.out.println("Order History:");

                Scanner orderScanner = new Scanner(ordersFile);

                while (orderScanner.hasNextLine()) {
                    String order = orderScanner.nextLine();
                    System.out.println(order);
                }

                orderScanner.close();
            } else {
                System.out.println("No order history available.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Orders file not found.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    //Allows user to check transaction history
    private static void chkTransHistory() {
        try {
            File transactionsFile = new File("C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\transactions.txt");

            if (transactionsFile.exists()) {
                // Display transaction history to the customer
                System.out.println("Transaction History:");

                Scanner transactionScanner = new Scanner(transactionsFile);

                while (transactionScanner.hasNextLine()) {
                    String transaction = transactionScanner.nextLine();
                    System.out.println(transaction);
                }

                transactionScanner.close();
            } else {
                System.out.println("No transaction history available.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transactions file not found.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    //Allows user to provide a review for each order
    private static void reviewEachOrder(){

    }
    //Allows user to reorder same ordered items from history
    private static void reorder() {
        String ORDER_HISTORY_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\orderHistory.txt";
        String MENU_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\orders.txt";
        try {
            File orderHistoryFile = new File(ORDER_HISTORY_FILE_PATH);
            File menuFile = new File(MENU_FILE_PATH);

            if (orderHistoryFile.exists() && menuFile.exists()) {
                // Display order history to the customer
                System.out.println("Order History:");

                Scanner orderHistoryScanner = new Scanner(orderHistoryFile);

                int orderNumber = 1;

                while (orderHistoryScanner.hasNextLine()) {
                    String order = orderHistoryScanner.nextLine();
                    System.out.println(orderNumber + ". " + order);
                    orderNumber++;
                }

                // Ask the customer to enter the number of the order to reorder
                System.out.println("Enter the number of the order to reorder: ");
                Scanner scanner = new Scanner(System.in);
                int selectedOrderNumber = scanner.nextInt();

                // Reorder items from the selected order
                Scanner orderHistoryScanner2 = new Scanner(orderHistoryFile);

                int orderCounter = 1;

                while (orderHistoryScanner2.hasNextLine()) {
                    String order = orderHistoryScanner2.nextLine();
                    if (orderCounter == selectedOrderNumber) {
                        // Extract items from the selected order and add them to the current order file
                        reorderItems(order, menuFile);
                        System.out.println("Items reordered successfully.");
                        return;
                    }
                    orderCounter++;
                }

                System.out.println("Invalid order number. Please try again.");

                orderHistoryScanner2.close();
            } else {
                System.out.println("No order history or menu available.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Order history or menu file not found.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void reorderItems(String order, File menuFile) throws IOException {
        String CURRENT_ORDER_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\customer_orders.txt";
        // Assuming each line in the order history file represents a list of item numbers separated by commas
        String[] itemNumbers = order.split(",");
        BufferedWriter writer = new BufferedWriter(new FileWriter(CURRENT_ORDER_FILE_PATH));

        // Read the menu file and add the selected items to the current order file
        Scanner menuScanner = new Scanner(menuFile);

        while (menuScanner.hasNextLine()) {
            String menuItem = menuScanner.nextLine();
            int menuItemNumber = Integer.parseInt(menuItem.split("\\.")[0].trim());

            for (String itemNumber : itemNumbers) {
                if (menuItemNumber == Integer.parseInt(itemNumber.trim())) {
                    writer.write(menuItem + "\n");
                    break;
                }
            }
        }

        writer.close();
        menuScanner.close();
    }

}
