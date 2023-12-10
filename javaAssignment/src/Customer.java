

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

public class Main {

    private static final String CREDENTIALS_FILE = "C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\accounts.txt";
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
                Admin.yura();
            } else {
                String role = userLogin(enteredUsername, enteredPassword);
                if (role != null) {
                    CurrentUser.setCurrentUser(new User(enteredUsername, role));
                    User currentUser = CurrentUser.getCurrentUser();  // Set the logged-in user
                    if ("customer".equals(role)) {
                        System.out.println("Logged in as customer");
                        Customer.Menu();
                    } else if ("vendor".equals(role)) {
                        System.out.println("Logged in as vendor");
                        //Vendor.vendorMenu(currentUser); // Pass the current user to vendorMenu
                    } else if ("delivery".equals(role)) {
                        System.out.println("Logged in as delivery runner");
                        DeliveryRunner.deliveryMenu(currentUser);
                    }
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            }
        }
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
