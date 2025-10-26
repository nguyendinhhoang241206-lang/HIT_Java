import java.sql.SQLOutput;
import java.util.Scanner;
public class Fibonaci {
    public static int f(int n) {
        if(n <= 2) {
            return 1;
        }
        else{
            return f(n - 1) + f(n - 2);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap n: ");
        int n = sc.nextInt();
        System.out.println(f(n));

    }
}
