import java.util.Scanner;
import service.UserService;
import service.AuthService;
public class Main {
    static Scanner sc = new Scanner(System.in);
    static AuthService auth = new AuthService();
    static UserService user = new UserService();
    public static void authMenu() {
        System.out.println("--- Menu Auth ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                System.out.println("--- Login ---");
                System.out.println("Enter username: ");
                String username = sc.nextLine();
                System.out.println("Enter password: ");
                String password = sc.nextLine();
                if(auth.login(username, password)) {
                    System.out.println("Welcome " + username);
                    userMenu();
                } else {
                    System.out.println("Wrong username or password");
                }
                break;
            case 2:
                System.out.println("--- Register ---");
                System.out.println("Enter username: ");
                String username1 = sc.nextLine();
                System.out.println("Enter password: ");
                String password1 = sc.nextLine();
                System.out.println("Enter email: ");
                String email1 = sc.nextLine();
                System.out.println("Enter phone number: ");
                String phoneNumber1 = sc.nextLine();
                if (auth.register(username1, password1, email1, phoneNumber1)) {
                    System.out.println("Welcome " + username1);
                    userMenu();
                } else {
                    System.out.println("Existed username or email or phone number");
                }
                break;
            case 3:
                System.out.println("--- Exit ---");
                break;
            default:
                System.out.println("Wrong choice");
        }
    }

    public static void userMenu() {
        System.out.println("--- Menu Users ---");
        System.out.println("1. Get User By ID");
        System.out.println("2. Get All Users");
        System.out.println("3. Change Password");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                System.out.println("--- Get User By ID ---");
                System.out.println("Enter userid: ");
                String userid2 = sc.nextLine();
                if(user.getUserById(userid2) != null) {
                    System.out.println(user.getUserById(userid2).toString());
                }
                else  {
                    System.out.println("User not found");
                }
                break;
            case 2:
                System.out.println("--- Get All Users ---");
                user.getAllUsers();
                break;
            case 3:
                System.out.println("--- Change Password ---");
                System.out.println("Enter username: ");
                String username3 = sc.nextLine();
                System.out.println("Enter newPassword: ");
                String password3 = sc.nextLine();
                System.out.println("Confirm newPassword: ");
                String confirmPassword3 = sc.nextLine();
                if (auth.changePassword(username3, password3, confirmPassword3)) {
                    System.out.println("Changed Password");
                } else {
                    System.out.println("No valid!");
                }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome User");
        authMenu();
    }
}