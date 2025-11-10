package controller;

import domain.Laptop;
import domain.Product;
import domain.SmartPhone;
import constants.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {
    public static Scanner sc = new Scanner(System.in);
    public static ArrayList<Product> products = new ArrayList<>();
    public void addProduct(Product product){
        products.add(product);
        System.out.println(Constant.Valid.ADD_VALID);
    }

    public static ArrayList<Laptop> laptops = new ArrayList<>();
    public static ArrayList<SmartPhone> smartPhones = new ArrayList<>();
    public void addProduct(String type, String name, String description, double price){
        if(type.equalsIgnoreCase("Laptop")){
            System.out.print("\n Enter RAM: ");
            String ram = sc.nextLine();
            System.out.print("\n Enter CPU: ");
            String cpu = sc.nextLine();
            laptops.add(new Laptop(name, description, price, ram, cpu));
            System.out.println(Constant.Valid.ADD_TYPE_VALID);
            products.add(new Laptop(name, description, price, ram, cpu));
            System.out.println(Constant.Valid.ADD_VALID);

        }
        else if(type.equalsIgnoreCase("SmartPhone")){
            System.out.print("\n Has 5G(Y / N): ");
            String has5G = sc.nextLine();
            boolean has5G1;
            if(has5G.equalsIgnoreCase("N")){
                has5G1 = false;
            } else {
                has5G1 = true;
            }
            smartPhones.add(new SmartPhone(name, description, price, has5G1));
            System.out.println(Constant.Valid.ADD_TYPE_VALID);
            products.add(new SmartPhone(name, description, price, has5G1));
            System.out.println(Constant.Valid.ADD_VALID);
        } else System.out.println(Constant.Invalid.INVALID_INPUT);
    }

    public void removeById(int id){
        if (products.isEmpty()){
            System.out.println(Constant.Invalid.INVALID_PRODUCTS);
        } else  {
            for (Product product : products){
                if (product.getId() == id){
                    products.remove(product);
                    System.out.println(Constant.Valid.REMOVE_VALID);
                    if (product instanceof Laptop) laptops.remove(product);
                    if (product instanceof SmartPhone) smartPhones.remove(product);
                    System.out.println(Constant.Valid.REMOVE_TYPE_VALID);
                }
            }
        }

    }

    public void getById(int id){
        for (Product p : products) {
            if (p.getId() == id) System.out.println(p.getInfo());
        }
    }

    public ArrayList<Product> getAllProducts(){
        return  products;
    }

}
