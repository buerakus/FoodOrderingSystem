import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;


public class Customer {
    private static final String TASK_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\Task.txt";
    Scanner scanner = new Scanner(System.in);

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
    private static void viewMenu(){

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

    }
    //Allows user to check order status, if any
    private static void chkOrderStatus(){

    }
    //Allows user to check personal ordering history
    private static void chkOrderHistory(){

    }
    //Allows user to check transaction history
    private static void chkTransHistory(){

    }
    //Allows user to provide a review for each order
    private static void reviewEachOrder(){

    }
    //Allows user to reorder same ordered items from history
    private static void reorder(){

    }
}
