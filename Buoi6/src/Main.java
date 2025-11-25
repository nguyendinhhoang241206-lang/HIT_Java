import constant.Constant;
import model.Book;
import model.Novel;
import model.TextBook;

import java.util.Scanner;
import service.impl.IBookServiceImpl;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static IBookServiceImpl bookService = new IBookServiceImpl();

    public static void displayMenu() {
        int choice;
        do {
            System.out.println("--- Menu ---\n" +
                    "1. Thêm sách\n" +
                    "2. Lấy sách theo ID\n" +
                    "3. Lấy tất cả sách theo thể loại (TextBook/Novel)\n" +
                    "4. Lấy tất cả sách\n" +
                    "5. Xóa sách theo ID\n" +
                    "6. Tổng giá trị thư viện \n" +
                    "0. Thoát");
            System.out.println("\n Nhập lựa chọn của bạn: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addBookMenu();
                    break;
                case 2:
                    System.out.println("Nhập ID sách cần tìm: ");
                    int idToGet = sc.nextInt();
                    sc.nextLine();
                    Book foundBook = bookService.getBookById(idToGet);
                    if (foundBook != null) {
                        System.out.println("Sách tìm thấy: " + foundBook.displayInfo());
                    } else {
                        System.out.println("Không tìm thấy sách với ID " + idToGet);
                    }
                    break;
                case 3:
                    System.out.println("Nhập thể loại sách cần lấy (TextBook/Novel): ");
                    String categoryType = sc.nextLine();
                    if (categoryType.equalsIgnoreCase("TextBook")) {
                        System.out.println("--- Danh sách TextBook ---");
                        bookService.getAllBooksByCategory(new TextBook());
                    } else if (categoryType.equalsIgnoreCase("Novel")) {
                        System.out.println("--- Danh sách Novel ---");
                        bookService.getAllBooksByCategory(new Novel());
                    } else {
                        System.out.println("Thể loại không hợp lệ.");
                    }
                    break;
                case 4:
                    System.out.println("--- Tất cả sách trong thư viện ---");
                    bookService.getAllBooks();
                    break;
                case 5:
                    System.out.println("Nhập ID sách cần xóa: ");
                    int idToDelete = sc.nextInt();
                    sc.nextLine();
                    if (bookService.deleteBookById(idToDelete) != null) {
                        System.out.println("Đã xóa sách có ID: " + idToDelete);
                    } else {
                        System.out.println("Không tìm thấy sách với ID: " + idToDelete);
                    }
                    break;
                case 6:
                    double totalValue = bookService.calculateTotalValue();
                    System.out.printf("Tổng giá trị thư viện là: %.2f\n", totalValue);
                    break;
                case 0:
                    System.out.println("Đã thoát chương trình.");
                    break;
                default:
                    System.out.println(Constant.Error.INVALID_CHOICE);
            }
            if (choice != 0) {
                System.out.println("\nNhấn Enter để tiếp tục...");
                sc.nextLine();
            }
        } while (choice != 0);
    }

    public static void addBookMenu() {
        System.out.println("Chọn loại sách muốn thêm:\n1. Sách giáo trình (TextBook)\n2. Tiểu thuyết (Novel)");
        System.out.print("Nhập lựa chọn (1 hoặc 2): ");
        int typeChoice = -1;

        if (sc.hasNextInt()) {
            typeChoice = sc.nextInt();
            sc.nextLine();
        } else {
            System.out.println(Constant.Error.INVALID_CHOICE);
            sc.nextLine();
            return;
        }

        System.out.println("Nhập tiêu đề: ");
        String name = sc.nextLine();
        System.out.println("Nhập tác giả: ");
        String author = sc.nextLine();
        System.out.println("Nhập giá: ");
        double price = sc.nextDouble();
        sc.nextLine();
        System.out.println("Nhập số lượng: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        int id = (int)(Math.random() * 1000);

        Book newBook = null;

        switch (typeChoice) {
            case 1:
                System.out.println("Nhập môn học (Subject): ");
                String subject = sc.nextLine();
                newBook = new TextBook(id, name, author, price, quantity, subject);
                break;
            case 2:
                System.out.println("Nhập thể loại (Genre): ");
                String genre = sc.nextLine();
                newBook = new Novel(id, name, author, price, quantity, genre);
                break;
            default:
                System.out.println("Lựa chọn loại sách không hợp lệ. Thao tác thêm sách bị hủy.");
                return;
        }

        if (newBook != null) {
            bookService.addBook(newBook);
            System.out.println("Đã thêm sách thành công: " + newBook.displayInfo());
        }
    }


    public static void main(String[] args) {
        displayMenu();
    }
}