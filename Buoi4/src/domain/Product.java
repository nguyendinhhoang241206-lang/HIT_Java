package domain;

public class Product {
    private int  id;
    private String name;
    private String description;
    private double price;

    public Product() {
    }

    public Product(String name, String description, double price) {
        id = (int)(Math.random() * 1000) + 1;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = (int)(Math.random() * 1000) + 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getById(int id){
        return name;
    }

    public String getInfo() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price;
    }

}
