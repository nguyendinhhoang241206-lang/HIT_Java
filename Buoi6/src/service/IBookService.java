package service;

import model.Book;

import java.util.ArrayList;


public interface IBookService {
    public ArrayList<Book> addBook(Book book);
    public Book getBookById(int id);
    public void getAllBooksByCategory(Object category);
    public ArrayList<Book> deleteBookById(int id);
    public void getAllBooks();
    public double calculateTotalValue();

}
