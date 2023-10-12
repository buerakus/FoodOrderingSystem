import java.util.Scanner;

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
                    //login()
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
}
//private static void login(){}
//private static void register(){}