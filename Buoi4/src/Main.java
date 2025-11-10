import controller.ProductController;
import domain.Product;

import java.util.Scanner;

public  class Main {
    public static Scanner sc = new Scanner(System.in);
    public static void Choose() {
        System.out.println("Choose:");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Get Products");
        System.out.println("4. Get All Product");
        System.out.println("5. Exit");
        System.out.println("Enter your choice: ");
        int choice = sc.nextInt();
        ProductController controller = new ProductController();
        switch (choice) {
            case 1:
                Product p = new Product();
                sc.nextLine();
                System.out.println("Enter product name: ");
                String name = sc.nextLine();
                p.setName(name);
                System.out.println("Enter product description: ");
                String description = sc.nextLine();
                p.setDescription(description);
                System.out.println("Enter product price: ");
                double price = sc.nextDouble();
                p.setPrice(price);
                sc.nextLine();
                System.out.println("Enter product type: ");
                String type = sc.nextLine();
                controller.addProduct(p);
                controller.addProduct(type, name, description, price);
                break;
            case 2:
                System.out.println("Enter product id: ");
                int id = sc.nextInt();
                controller.removeById(id);
                break;
            case 3:
                System.out.println("Enter product id: ");
                int id1 = sc.nextInt();
                controller.getById(id1);
                break;
            case 4:
                System.out.println(controller.getAllProducts());
            case 5:
                System.exit(0);
                break;
            default: System.out.println("Wrong choice");
        }

    }

    public static void main(String[] args) {
        Choose();
    }

}