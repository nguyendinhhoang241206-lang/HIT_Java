package model;

public class Book implements Borrowable {
    private int id;
    private String title;
    private String author;
    private double price;
    private int quantity;

    public Book() {
    }

    public Book(int id, String title, String author, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String displayInfo() {
        return  "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", quantity=" + quantity;
    }

    @Override
    public boolean borrow(int quantity) {
        if(this.quantity < quantity) {
            return false;
        }
        this.quantity -= quantity;
        return true;
    }

    @Override
    public int getAvailableQuantity() {
        return quantity;
    }
}
