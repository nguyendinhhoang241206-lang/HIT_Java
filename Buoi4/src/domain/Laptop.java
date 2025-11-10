package domain;

public class Laptop extends Product {
    private String ram;
    private String cpu;

    public Laptop(){
        super();
    }

    public Laptop(String name, String description, double price, String ram, String cpu) {
        super(name, description, price);
        this.ram = ram;
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getInfo() {
        return "Laptop{" + super.getInfo() +
                "ram='" + ram + '\'' +
                ", cpu='" + cpu + '\'' +
                '}';
    }
}
