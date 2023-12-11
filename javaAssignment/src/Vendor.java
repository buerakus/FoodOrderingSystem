public class Vendor {

    private static final String TASK_FILE_PATH = "orders.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final String TASK_FILE_PATHVC = "ordersvc.txt";
    private static final String REVIEWS_FILE_PATH = "reviews.txt";
    
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
                    readCReview(currentUser);
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
    System.out.println("Enter the ID of the product to be deleted:");
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
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("The product with the specified ID was not found or does not belong to you.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Task file not found.");
    } catch (IOException e) {
        System.out.println("Error updating task file.");
    }
}


    
    public static void acceptCancelOrder(User currentUser) {
    String vendorPrefix = currentUser.getUsername(); // Vendor's username from currentUser object
    Scanner scanner = new Scanner(System.in);

    List<String> orders = new ArrayList<>();
    boolean orderFound = false;

    try {
        File ordersFile = new File(TASK_FILE_PATHVC);
        Scanner fileScanner = new Scanner(ordersFile);

        // Display all orders for the current vendor
        System.out.println("Orders for vendor: " + vendorPrefix);
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.println("| Order ID | Vendor Name | Customer ID | Item ID | Item Name | Order Date | Price | Pickup Method | Status |");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");
            if (columns[1].trim().equals(vendorPrefix)) {
                // Display the order
                System.out.printf("| %-8s | %-12s | %-11s | %-7s | %-9s | %-10s | %-5s | %-13s | %-6s | \n",
                                  columns[0].trim(), columns[1].trim(), columns[2].trim(), columns[3].trim(), 
                                  columns[4].trim(), columns[5].trim(), "RM" + columns[6].trim(), columns[7].trim(), columns[8].trim());
                orderFound = true;
            }
            orders.add(line); // Add all lines to the orders list
        }
        
        fileScanner.close();
        System.out.println("----------------------------------------------------------------------------------------------------------");
        
        if (!orderFound) {
            System.out.println("No orders found for vendor: " + vendorPrefix);
            return; // No orders found for this vendor, exit the method
        }

        // Prompt the vendor for the order ID to accept/reject
        System.out.println("Enter order ID to accept/reject:");
        String orderId = scanner.nextLine();
        boolean orderFoundAndUpdated = false;

        // Iterate through the list to find and update the status of the order
        for (int i = 0; i < orders.size(); i++) {
            String line = orders.get(i);
            String[] columns = line.split(",");
            if (columns[0].trim().equals(orderId) && columns[1].trim().equals(vendorPrefix)) {
                System.out.println("The order has been found. Accept an order? (yes/no):");
                String decision = scanner.nextLine().trim().toLowerCase();
                String newStatus = decision.equals("yes") ? "order accepted" : "order canceled";
                columns[columns.length - 1] = newStatus; // Update the status
                orders.set(i, String.join(",", columns)); // Update the line in the orders list
                orderFoundAndUpdated = true;
            }
        }

        // If the order was found and updated, write the updated list back to the file
        if (orderFoundAndUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE_PATHVC, false))) {
                for (String order : orders) {
                    writer.write(order);
                    writer.newLine();
                }
            }
            System.out.println("Order status updated.");
        } else {
            System.out.println("The order with the specified ID was not found or does not belong to you.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Order file not found.");
    } catch (IOException e) {
        System.out.println("Error updating order file.");
    }
}


   
    public static void readCReview(User currentUser) {
    String vendorPrefix = currentUser.getUsername(); // Vendor's username from currentUser object

    try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE_PATH))) {
        String line;
        boolean foundReviews = false;

        System.out.println("Reviews for " + vendorPrefix + ":");
        while ((line = reader.readLine()) != null) {
            String[] reviewDetails = line.split("/");

            // Check if the review is for the current vendor
            if (reviewDetails[0].trim().equals(vendorPrefix)) {
                // Display review details
                System.out.println("Order ID: " + reviewDetails[1].trim() + ", Customer: " + reviewDetails[3].trim() + ", Review: " + reviewDetails[4].trim());
                foundReviews = true;
            }
        }

        if (!foundReviews) {
            System.out.println("No reviews found.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Review file not found.");
    } catch (IOException e) {
        System.out.println("Error reading the feedback file.");
    }
}

    private static void revenueDash() {
        
    }
    
    public static void updateOrderStatus(User currentUser) {
    String vendorPrefix = currentUser.getUsername(); // Vendor's username from the currentUser object
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter order's ID, to change it to 'pending acceptance':");
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

            // Check if the line corresponds to the order ID and the vendor
            if (columns[0].trim().equals(orderId) && columns[1].trim().equals(vendorPrefix)) {
                // Assuming the last column is the status
                String currentStatus = columns[columns.length - 1].trim();
                if ("order ready".equals(currentStatus)) {
                    // Update the status to "заказ готов"
                    columns[columns.length - 1] = "pending acceptance";
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
            System.out.println("Order updated to 'pending acceptance'.");
        } else {
            System.out.println("The order with the specified ID was not found or does not belong to you.");
        }

    } catch (FileNotFoundException e) {
        System.out.println("Task file not found.");
    } catch (IOException e) {
        System.out.println("Error updating task file.");
    }
}

    
    
}
