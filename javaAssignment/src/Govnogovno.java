package govnogovno;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;

class User {
    private String username;
    private String role;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
}

// CurrentUser class for managing the state of the logged-in user
class CurrentUser {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearCurrentUser() {
        currentUser = null;
    }
}

public class Govnogovno {

    private static final String CREDENTIALS_FILE = "user_credentials.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to the Account System");
            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            if ("admin".equals(enteredUsername) && "admin".equals(enteredPassword)) {
                CurrentUser.setCurrentUser(new User("admin", "admin"));  // Set admin as the current user
                adminMenu();
            } else {
                String role = userLogin(enteredUsername, enteredPassword);
                if (role != null) {
                    CurrentUser.setCurrentUser(new User(enteredUsername, role));
                    User currentUser = CurrentUser.getCurrentUser();  // Set the logged-in user
                    if ("customer".equals(role)) {
                        System.out.println("Logged in as customer");
                        // CustomerMenu();
                    } else if ("vendor".equals(role)) {
                        System.out.println("Logged in as vendor");
                        Vendor.vendorMenu(currentUser); // Pass the current user to vendorMenu
                    } else if ("delivery".equals(role)) {
                        System.out.println("Logged in as delivery runner");
                        // DeliveryMenu();
                    }
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            }
        }
    }

    private static void adminMenu() {
        System.out.println("Admin Menu:");
        System.out.println("1. Create Customer");
        System.out.println("2. Create Vendor");
        System.out.println("3. Create Delivery");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                registerUser("customer");
                break;
            case "2":
                registerUser("vendor");
                break;
            case "3":
                registerUser("delivery");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void registerUser(String role) {
        System.out.print("Enter username for " + role + ": ");
        String username = scanner.nextLine();
        System.out.print("Enter password for " + role + ": ");
        String password = scanner.nextLine();

        try (FileWriter fw = new FileWriter(CREDENTIALS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(role + "," + username + "," + password);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }

        System.out.println(role + " user registered successfully.");
    }

    private static String userLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 3 && credentials[1].equals(username) && credentials[2].equals(password)) {
                    return credentials[0]; // Returning the role
            }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
        }
        return null;
    }
}


