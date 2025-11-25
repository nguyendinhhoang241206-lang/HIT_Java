import constant.Constant;
import model.Developer;
import model.Device;
import model.Employee;
import model.Tester;
import service.impl.IEmployeeServiceImpl;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static IEmployeeServiceImpl ev =  new IEmployeeServiceImpl();
    public ArrayList<Employee> employees = new ArrayList<>();
    public static void employeeMenu() {
        int choice;
        do {
            System.out.println("--- Menu ---\n" +
                    "1. In ra danh sach tat cac nhan vien\n" +
                    "2. In ra thong tin nhan vien theo ID\n" +
                    "3. Loc ra danh sach nhan vien theo ten\n" +
                    "4. Thoat\n");
            System.out.println("\n Nhập lựa chọn của bạn: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    ev.getAllEmployees();
                    break;
                case 2:
                    System.out.println("\n Nhap id can tim: ");
                    String id = sc.nextLine();
                    if(ev.getEmployeeById(id) != null){
                        System.out.println("\n Tim thanh cong " + ev.getEmployeeById(id));
                    } else System.out.println("\n Khong ton tai id");
                    break;
                case 3:
                    System.out.println("\n Nhap ten can loc: ");
                    String name = sc.nextLine();
                    if(ev.getEmployeeByName(name) != null){
                        System.out.println("\n Loc thanh cong ");
                        ev.getEmployeeByName(name);
                    } else System.out.println("\n Khong ton tai name");
                    break;
                case 4:
                    System.out.println("\n Da thoat menu");
                default:
                    System.out.println(Constant.Error.INVALID_CHOICE);
            }
            if (choice != 0) {
                sc.nextLine();
            }
        } while (choice != 0);
    }

    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee1 = new Developer("01", "Hoang",10, 10000000, new Device("01", "01"), 10) ;
        Employee employee2 = new Tester("02", "Dinh",11, 20000000, new Device("02", "02"), 20) ;
        Employee employee3 = new Developer("03", "Nguyen",12, 30000000, new Device("03", "03"), 30) ;
        Employee employee4 = new Tester("04", "Dung",13, 40000000, new Device("04", "04"), 40 ) ;
        Employee employee5 = new Developer("05", "Quan",14, 50000000, new Device("05", "05"), 50 );
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        employees.add(employee4);
        employees.add(employee5);
        employeeMenu();
    }
}