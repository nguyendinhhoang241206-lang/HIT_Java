package model;

public interface Borrowable {
    public boolean borrow(int quantity);
    public int getAvailableQuantity();
}
