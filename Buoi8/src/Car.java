public class Car implements Runnable {
    private String name;
    private double speed;

    public Car() {
    }

    public Car(String name, double speed) {
        this.name = name;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 30; i++) {
                int rd = (int) (Math.random() * 100 + 1);
                if (rd < 10) {
                    throw new BrokenCarException("Xe " + name + " bị nổ lốp!");
                }
                Thread.sleep((int) (Math.random() * (this.speed - 100 + 1)) + 100);
                System.out.println("Xe " + name + " da chay duoc " + i + "km.");
            }
            System.out.println("Xe " + name + " đã về đích!");
        } catch (BrokenCarException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Xe " + name + " bị gián đoạn!");
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", speed=" + speed +
                '}';
    }
}

