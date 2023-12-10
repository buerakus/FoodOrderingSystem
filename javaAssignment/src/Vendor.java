package govnogovno;

import java.io.*;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class Vendor {

    private static final String TASK_FILE_PATH = "orders.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final String TASK_FILE_PATHVC = "ordersvc.txt";
    
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
                    acceptCancelOrder(currentUser);
                    break;
                case 6:
                    updateOrderStatus(currentUser);
                    break;
                case 7:
                    readCReview();
                    break;
                case 8:
                    revenueDash();
                    break;
                case 0:
                    System.out.println("Exiting..");
                    
                    return;
                default:
                    System.out.println("Wrong action. Try again.");
            }
        }
    }
   private static void createItems(User currentUser) {
        String vendorPrefix = currentUser.getUsername() + ", "; // Prefix with vendor username
        try (FileWriter fw = new FileWriter(TASK_FILE_PATH, true)) {
            System.out.println("Enter item details:");
            String itemDetails = scanner.nextLine();
            System.out.println("Enter item ID:");
            String itemID = scanner.nextLine();
            System.out.println("Enter item price:");
            String itemPrice = scanner.nextLine();
            // Write the item prefixed by the vendor's username
            fw.write(itemID + ", " + vendorPrefix + itemDetails + ", " + itemPrice + System.lineSeparator());
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
    boolean itemFound = false;
    try (BufferedReader br = new BufferedReader(new FileReader(TASK_FILE_PATH))) {
        String line;
        while ((line = br.readLine()) != null) {
            // Check if the line belongs to the current vendor and matches the item ID
            if (line.startsWith(vendorPrefix + itemIdToUpdate + ",")) {
                // Replace with new item details
                items.add(itemIdToUpdate + "," + vendorPrefix + newItemDetails + "," + newItemPrice);
                itemFound = true;
            } else {
                // Keep other items as they are
                items.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while reading the file.");
        e.printStackTrace();
    }

    // If the item was found and updated, write the updated items back to the file
    if(itemFound) {
        try (FileWriter fw = new FileWriter(TASK_FILE_PATH, false)) {
            for (String item : items) {
                fw.write(item + System.lineSeparator());
            }
            System.out.println("Item updated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    } else {
        System.out.println("Item with ID " + itemIdToUpdate + " not found or does not belong to you.");
    }
}


    public static void deleteItems(User currentUser) {
    String vendorPrefix = currentUser.getUsername(); // Vendor's username from the currentUser object
    Scanner scanner = new Scanner(System.in);
    System.out.println("Введите ID удаляемого товара:");
    String itemId = scanner.nextLine();

    List<String> tasks = new ArrayList<>();
    boolean itemDeleted = false;

    try {
        File taskFile = new File(TASK_FILE_PATH);
        Scanner fileScanner = new Scanner(taskFile);

        // Read all tasks from the file
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");

            // Check if the line corresponds to the vendor and the item ID
            if (columns[1].trim().equals(vendorPrefix) && columns[0].trim().equals(itemId)) {
                itemDeleted = true; // Mark for deletion
                continue; // Skip adding this line to the tasks list
            }
            tasks.add(line);
        }
        fileScanner.close();

        // If the item was marked for deletion, write the updated list back to the file
        if (itemDeleted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE_PATH, false))) {
                for (String task : tasks) {
                    writer.write(task);
                    writer.newLine();
                }
            }
            System.out.println("Товар успешно удален.");
        } else {
            System.out.println("Товар с указанным ID не найден или не принадлежит вам.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Файл задач не найден.");
    } catch (IOException e) {
        System.out.println("Ошибка при обновлении файла задач.");
    }
}


    
    public static void acceptCancelOrder(User currentUser) {
    String vendorPrefix = currentUser.getUsername(); // Vendor's username from the currentUser object
    Scanner scanner = new Scanner(System.in);
    System.out.println("Введите ID заказа для принятия/отклонения:");
    String orderId = scanner.nextLine();

    List<String> tasks = new ArrayList<>();
    boolean orderUpdated = false;

    try {
        File taskFile = new File(TASK_FILE_PATHVC);
        Scanner fileScanner = new Scanner(taskFile);

        // Read all tasks from the file
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");

            // Check if the line corresponds to the vendor and the order ID
            if (columns[1].trim().equals(vendorPrefix) && columns[2].trim().equals(orderId)) {
                System.out.println("Заказ найден. Принять заказ? (yes/no):");
                String decision = scanner.nextLine().trim().toLowerCase();
                String newStatus = decision.equals("yes") ? "заказ принят" : "заказ отклонен";
                columns[columns.length - 1] = newStatus; // Update the status
                line = String.join(",", columns);
                orderUpdated = true;
            }
            tasks.add(line);
        }
        fileScanner.close();

        // If the order was updated, write the updated list back to the file
        if (orderUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE_PATH, false))) {
                for (String task : tasks) {
                    writer.write(task);
                    writer.newLine();
                }
            }
            System.out.println("Статус заказа обновлен.");
        } else {
            System.out.println("Заказ с указанным ID не найден или не принадлежит вам.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Файл задач не найден.");
    } catch (IOException e) {
        System.out.println("Ошибка при обновлении файла задач.");
    }
}

   
    private static void readCReview() {
        
    }
    private static void revenueDash() {
        
    }
    
    public static void updateOrderStatus(User currentUser) {
    String vendorPrefix = currentUser.getUsername(); // Vendor's username from the currentUser object
    Scanner scanner = new Scanner(System.in);
    System.out.println("Введите ID заказа, который готов:");
    String orderId = scanner.nextLine();

    List<String> tasks = new ArrayList<>();
    boolean orderUpdated = false;

    try {
        File taskFile = new File(TASK_FILE_PATHVC);
        Scanner fileScanner = new Scanner(taskFile);

        // Read all tasks from the file
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");

            // Trim spaces and check if the line corresponds to the vendor and the order ID
            if (columns[1].trim().equals(vendorPrefix) && columns[2].trim().equals(orderId)) {
                if ("заказ принят".equals(columns[columns.length - 1].trim())) {
                    columns[columns.length - 1] = "заказ готов";  // Update the status to "order ready"
                    line = String.join(",", columns);
                    orderUpdated = true;
                }
            }
            tasks.add(line);
        }
        fileScanner.close();

        // If the order was updated, write the updated list back to the file
        if (orderUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE_PATHVC, false))) {
                for (String task : tasks) {
                    writer.write(task);
                    writer.newLine();
                }
            }
            System.out.println("Статус заказа обновлен на 'заказ готов'.");
        } else {
            System.out.println("Заказ с указанным ID не найден или не принадлежит вам.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Файл задач не найден.");
    } catch (IOException e) {
        System.out.println("Ошибка при обновлении файла задач.");
    }
}
    
    
}
