import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Main {
    public static void main(String[] args) {
        Scanner Sc = new Scanner(System.in);
        int menu = 1;
        //while loop will continue until quit options is chosen

        while(menu == 1){
            System.out.println("Welcome to Madraimov Food Ordering System! Please enter option : ");
            System.out.println("1. Login\n2. Register3\n3. Quit");

            switch (Sc.nextInt()){
                case 1:
                    login();
                    break;
                case 2:
                    //register()
                    break;
                case 3:
                    menu++;
                    break;
            }
        }
    }
    private static void login() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String username = sc.next();
        System.out.println("Enter your password: ");
        String password = sc.next();

        try {
            Scanner fileScanner = new Scanner(new File("C:\\Users\\lorde\\IdeaProjects\\OODJAssignment\\src\\accounts.txt"));
            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    System.out.println("Login successful!");
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Invalid username or password. Please try again.");
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: accounts.txt not found.");
        }
    }

}

//private static void login(){}
//private static void register(){}
