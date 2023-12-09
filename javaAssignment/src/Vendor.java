import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class Vendor {

    private static final String TASK_FILE_PATH = "orders.txt";
    private static final Scanner scanner = new Scanner(System.in);
    
    
    public static void vendorMenu(User currentUser) {
        
        if (currentUser == null || !"vendor".equals(currentUser.getRole())) {
            System.out.println("Access denied. Only vendors can access this menu.");
            return; // Exit the method if the user is not a vendor
        }
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
                    createItems(currentUser);
                    break;
                case 2:
                    readItems(currentUser);
                    break;
                case 3:
                    updateItems(currentUser);
                    break;
                case 4:
                    deleteItems(currentUser);
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
    private static void createItems(User currentUser) {
        String vendorPrefix = currentUser.getUsername() + ","; // Prefix with vendor username
        try (FileWriter fw = new FileWriter(TASK_FILE_PATH, true)) {
            System.out.println("Enter item details:");
            String itemDetails = scanner.nextLine();
            System.out.println("Enter item ID:");
            String itemID = scanner.nextLine();
            System.out.println("Enter item price:");
            String itemPrice = scanner.nextLine();
            // Write the item prefixed by the vendor's username
            fw.write(vendorPrefix + itemID + "," + itemDetails + "," + itemPrice + System.lineSeparator());
            System.out.println("Item created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    
    private static void readItems(User currentUser) {
        String vendorPrefix = currentUser.getUsername() + ","; // Prefix to look for
        try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Only print items that belong to the current vendor
                if (line.startsWith(vendorPrefix)) {
                    System.out.println(line.substring(vendorPrefix.length())); // Remove the prefix before printing
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }
    private static void updateItems(User currentUser) {
    String vendorPrefix = currentUser.getUsername() + ","; // Prefix to identify the vendor's items

    System.out.println("Enter the item ID to update:");
    String itemIdToUpdate = scanner.nextLine();
    System.out.println("Enter new item details:");
    String newItemDetails = scanner.nextLine();
    System.out.println("Enter new item price:");
    String newItemPrice = scanner.nextLine();

    List<String> items = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
        String line;
        while ((line = br.readLine()) != null) {
            // Check if the line belongs to the current vendor and matches the item ID
            if (line.startsWith(vendorPrefix + itemIdToUpdate + ",")) {
                // Replace with new item details
                items.add(vendorPrefix + itemIdToUpdate + "," + newItemDetails + "," + newItemPrice);
            } else {
                // Keep other items as they are
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

    private static void deleteItems(User currentUser) {
    String vendorPrefix = currentUser.getUsername() + ","; // Prefix to identify the vendor's items

    System.out.println("Enter the item ID to delete:");
    String itemIdToDelete = scanner.nextLine();

    List<String> items = new ArrayList<>();
    boolean found = false;
    try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
        String line;
        while ((line = br.readLine()) != null) {
            // Check if the line belongs to the current vendor and matches the item ID
            if (line.startsWith(vendorPrefix + itemIdToDelete + ",")) {
                found = true; // Mark as found but don't add to the list, effectively deleting it
            } else {
                // Keep other items as they are
                items.add(line);
            }
        }
        if (!found) {
            System.out.println("Item ID not found or it doesn't belong to you.");
        }
    } catch (IOException e) {
        System.out.println("An error occurred while reading the file.");
        e.printStackTrace();
    }

    // Write the remaining items back to the file
    try (FileWriter fw = new FileWriter(TASK_FILE_PATH, false)) {
        for (String item : items) {
            fw.write(item + System.lineSeparator());
        }
        if (found) {
            System.out.println("Item deleted successfully.");
        }
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
  
    
    
}
