package service.impl;

import model.Book;
import model.Novel;
import model.TextBook;
import service.IBookService;

import java.util.ArrayList;

public class IBookServiceImpl implements IBookService {
    public static ArrayList<Book> books = new ArrayList<>();
    @Override
    public ArrayList<Book> addBook(Book book) {
        books.add(book);
        return books;
    }
    @Override
    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) return book;
        }
        return null;
    }
    @Override
    public void getAllBooksByCategory(Object category) {
        for (Book book : books) {
            if(category instanceof TextBook && book instanceof TextBook){
                System.out.println(book.displayInfo());
            } else if(category instanceof Novel && book instanceof Novel){
                System.out.println(book.displayInfo());
            }
        }
    }
    @Override
    public ArrayList<Book> deleteBookById(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                books.remove(i);
                return books;
            }
        }
        return null;
    }
    @Override
    public void getAllBooks() {
        for (Book book : books) {
            System.out.println(book.displayInfo());
        }
    }
    public double calculateTotalValue() {
        double total = 0;
        for (Book book : books) {
            total += book.getPrice() * book.getQuantity();
        }
        return total;
    }

}
