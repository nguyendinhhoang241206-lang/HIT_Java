
import java.util.Scanner;
public class Main {
    public static void Nhap(Scanner sc, int n, int[] arr) {
        System.out.println("\n Nhập giá trị: ");
        for(int i= 0; i < n; i++) {
            System.out.print("\n Nhập phần tử thứ " + (i + 1) + ": ");
            arr[i] = sc.nextInt();
        }
    }

    public static void Xuat(int[] arr) {
        for (int x : arr) {
            System.out.print(x + " ");
        }
    }

    public static int Sum(int[] arr) {
        int sum = 0;
        for (int x : arr) {
            sum += x;
        }
        return sum;
    }

    public static void OutPut(int n, int[] arr) {
        int Max = arr[0];
        for (int i = 1; i < n; i++) {
            if (Max < arr[i]) {
                Max = arr[i];
            }
        }
        int Min = arr[0];
        for (int i = 1; i < n; i++) {
            if (Min > arr[i]) {
                Min = arr[i];
            }
        }
        System.out.println("Phần tử lớn nhất trong mảng là: " + Max);
        System.out.println("Phần tử nhỏ nhất trong mảng là: " + Min);
    }

    public static void Sort(int n, int[] arr) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void Text(int n, int[] arr) {
        int soNgTo = 1;
        for(int i = 0; i < n; i++) {
            for(int j = 2; j <= Math.sqrt(arr[i]); j++) {
                if (arr[i] % j != 0) {
                    if(soNgTo < arr[i]) {
                        soNgTo = arr[i];
                    }
                }
            }
        }
        if(soNgTo == 1) {
            System.out.println("Không có");
        } else {
            System.out.println("Số nguyên tố lớn nhất là: " + soNgTo);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập vào số lượng phần tử: ");
        int n = sc.nextInt();
        int[] arr = new int[n];
        Nhap(sc, n, arr);
        System.out.println("\n Mảng đã nhập là: ");
        Xuat(arr);
        System.out.println("\n      --- MENU ---");
        System.out.println("1. Tính tổng các phần tử trong mảng.");
        System.out.println("2. In ra phần tử lớn nhất và nhỏ nhất.");
        System.out.println("3. Sắp xếp theo chiều tăng dần.");
        System.out.println("4. In ra số nguyên tố trong mảng.");
        System.out.println("5. Kết thúc.");
        System.out.print("Nhập lựa chọn: ");
        int chon = sc.nextInt();
        switch (chon) {
            case 1:
                System.out.println("\n Tổng các phần tử trong mảng là: " + Sum(arr));
                break;
            case 2:
                OutPut(n, arr);
                break;
            case 3:
                Sort(n, arr);
                System.out.print("\n Mảng sau khi sắp xếp tăng dần: ");
                Xuat(arr);
                break;
            case 4:
                Text(n, arr);
                break;
            case 5:
                System.out.println("Kết thúc chương trình.");
                break;
            default:
                System.out.println("Không có lựa chọn này !");
        }

    }
}
