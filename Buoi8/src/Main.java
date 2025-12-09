public class Main {
    public static void main(String[] args) {
        Car car1 = new Car("Ferrari", 500);
        Car car2 = new Car("Lamborghini", 450);
        Car car3 = new Car("BMW", 550);

        Thread t1 = new Thread(car1);
        Thread t2 = new Thread(car2);
        Thread t3 = new Thread(car3);

        t1.start();
        t2.start();
        t3.start();
    }
}
