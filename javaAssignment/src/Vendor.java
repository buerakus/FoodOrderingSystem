import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class Vendor {

    private static final String TASK_FILE_PATH = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\orders.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void vendorMenu() {
        while (true) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Choose action:");
            System.out.println("1. create item");
            System.out.println("2. read item");
            System.out.println("3. update item");
            System.out.println("4. delete item");
            System.out.println("5. accept/cancel order");
            System.out.println("6. update order status");
            System.out.println("6. check order history ");
            System.out.println("7. read customer review");
            System.out.println("8. revenue dashboard");
            System.out.println("0. Exit.");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            int choiceV = scanner.nextInt();
            scanner.nextLine();
            switch (choiceV) {
                case 1:
                    createItems();
                    break;
                case 2:
                    readItems();
                    break;
                case 3:
                    updateItems();
                    break;
                case 4:
                    deleteItems();
                    break;
                case 5:
                    acceptCancelOrder();
                    break;
                case 6:
                    updateOrder();
                    break;
                case 7:
                    readCReview();
                    break;
                case 8:
                    revenueDash();
                    break;
                case 0:
                    System.out.println("Exiting..");
                    scanner.close();
                    return;
                default:
                    System.out.println("Wrong action. Try again.");
            }
        }
    }
    private static void createItems() {
        try (FileWriter fw = new FileWriter(TASK_FILE_PATH, true)) {
            System.out.println("Enter item details:");
            String itemDetails = scanner.nextLine();
            System.out.println("Enter item ID");
            String itemID = scanner.nextLine();
            System.out.println("Enter item price");
            String itemPrice = scanner.nextLine();
            fw.write(itemID + "," + itemDetails + "," + itemPrice + System.lineSeparator());
            System.out.println("Item created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    private static void readItems() {
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }

    private static void updateItems() {
        System.out.println("Enter the item detail to update:");
        String oldItem = scanner.nextLine();

        // Prompt user for new item details
        System.out.println("Enter new item details:");
        String newItemDetails = scanner.nextLine();

        System.out.println("Enter item ID");
        String itemID = scanner.nextLine();
        System.out.println("Enter item price");
        String itemPrice = scanner.nextLine();

        // Read the current items
        List<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(oldItem)) {
                    items.add(itemID + "," + newItemDetails + "," + itemPrice);
                } else {
                    items.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        // Write the updated items back to the file
        try (FileWriter fw = new FileWriter(TASK_FILE_PATH, false)) {
            for (String item : items) {
                fw.write(item + System.lineSeparator());
            }
            System.out.println("Item updated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void deleteItems() {
        System.out.println("Enter the item detail to delete:");
        String itemToDelete = scanner.nextLine();

        // Read the current items
        List<String> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(itemToDelete)) {
                    items.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        // Write the updated items back to the file
        try (FileWriter fw = new FileWriter(TASK_FILE_PATH, false)) {
            for (String item : items) {
                fw.write(item + System.lineSeparator());
            }
            System.out.println("Item deleted successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }


    private static void acceptCancelOrder() {

    }
    private static void updateOrder() {

    }
    private static void readCReview() {

    }
    private static void revenueDash() {

    }

    public static void main(String[] args) {
        Vendor.vendorMenu();
    }

}
